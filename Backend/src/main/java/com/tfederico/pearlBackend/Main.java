package com.tfederico.pearlBackend;

import com.ibm.watson.developer_cloud.natural_language_understanding.v1.NaturalLanguageUnderstanding;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.KeywordsResult;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.Classifier;
import com.tfederico.libris.image.ibm.contract.IIBMCustomClassifierUtility;
import com.tfederico.libris.image.ibm.visualRecognition.IBMCustomClassifierUtility;
import com.tfederico.libris.text.naturalLanguageUnderstanding.contract.IIBMNaturalLanguageUnderstandingUtility;
import com.tfederico.libris.text.naturalLanguageUnderstanding.ibm.IBMNaturalLanguageUnderstandingUtility;
import com.tfederico.pearlBackend.db.PaintingDbReader;
import com.tfederico.pearlBackend.db.contract.IPaintingDbReader;
import com.tfederico.pearlBackend.europeana.Querier;
import com.tfederico.pearlBackend.europeana.QueryBuilder;
import com.tfederico.pearlBackend.europeana.QueryResultParser;
import com.tfederico.pearlBackend.europeana.contract.IQuerier;
import com.tfederico.pearlBackend.europeana.contract.IQueryBuilder;
import com.tfederico.pearlBackend.exceptions.UnknownMuseumException;
import com.tfederico.pearlBackend.webCrawler.WebCralwer;
import com.tfederico.pearlBackend.webCrawler.contract.IWebCrawler;
import eu.europeana.api.client.exception.EuropeanaApiProblem;
import eu.europeana.api.client.model.EuropeanaApi2Results;


import java.io.*;
import java.util.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {

    public static void main(String[] args) throws IOException, UnknownMuseumException, EuropeanaApiProblem {

        setCredentials("credentials.csv");
        long start = System.currentTimeMillis();

        IPaintingDbReader paintingDbReader = new PaintingDbReader();
        paintingDbReader.openDatabase();
        paintingDbReader.readDatabase();

        ArrayList<String> paintings = paintingDbReader.getPaintings();
        ArrayList<String> painters = paintingDbReader.getAuthors();
        ArrayList<String> museums = paintingDbReader.getMuseums();

        IQueryBuilder queryBuilder = new QueryBuilder();

        ArrayList<String> descriptions = new ArrayList<>();
        ArrayList<String> creator = new ArrayList<>();
        ArrayList<String> thumbnailsURL = new ArrayList<>();

        IWebCrawler webCrawler = new WebCralwer();

        for(int i = 0; i < paintings.size(); i++){
            callEuropeana(queryBuilder, museums.get(i), paintings.get(i), descriptions,
                    creator, thumbnailsURL);
            //download(webCrawler, painters.get(i), paintings.get(i));
        }



        //trainModels(paintings);

        IIBMNaturalLanguageUnderstandingUtility nlu = new IBMNaturalLanguageUnderstandingUtility();
        ArrayList<String> keyword = new ArrayList<>(descriptions.size());


        for (String description : descriptions) {
            List<KeywordsResult> keywordsResultList = nlu.getKeywordsFromText(description);
            StringBuilder builder = new StringBuilder();
            Set<String> set = new HashSet<>();

            for (KeywordsResult keywordsResult : keywordsResultList) //remove duplicates
                set.add(keywordsResult.getText());

            for (String s : set)
                builder.append(s.toLowerCase()).append(" ");

            int length = builder.toString().length();
            keyword.add(builder.toString().substring(0, length - 1));
        }
        saveResultsInDB(paintings, painters, descriptions, thumbnailsURL, museums, keyword);


        System.out.println("Required time: "+(System.currentTimeMillis()-start)+" ms");

    }

    private static void setCredentials(String path) throws IOException {
        FileReader f = new FileReader(path);

        String line;
        BufferedReader br = new BufferedReader(f);
        ArrayList<String> keys = new ArrayList<>();
        while ((line = br.readLine()) != null) {
            // use comma as separator
            String[] data = line.split(",");
            keys.add(data[1]);
        }

        IBMCustomClassifierUtility.setSubscriptionKey(keys.get(0));
        IBMNaturalLanguageUnderstandingUtility.setUsernameAndPassword(keys.get(1),keys.get(2));

        br.close();
    }

    private static void callEuropeana(IQueryBuilder queryBuilder, String museum, String painting,
                                      ArrayList<String> descriptions, ArrayList<String> creator,
                                      ArrayList<String> thumbnailsURL) throws UnknownMuseumException, IOException, EuropeanaApiProblem {
        queryBuilder.createNewQuery();
        queryBuilder.setTitle(painting);

        if(Objects.equals(museum, "Mauritshuis"))
            queryBuilder.setCollectionName("2021672_Ag_NL_DigitaleCollectie_Mauritshuis");
        else if (Objects.equals(museum, "Rijksmuseum"))
            queryBuilder.setCollectionName("90402_M_NL_Rijksmuseum");
        else
            throw new UnknownMuseumException();

        QueryResultParser resultParser;

        EuropeanaApi2Results results;
        IQuerier querier = new Querier();
        results = querier.search(queryBuilder.getQuery(),1,0);
        resultParser = new QueryResultParser(results);

        descriptions.add(resultParser.getDescriptions().get(0));
        creator.add(resultParser.getCreators().get(0));
        thumbnailsURL.add(resultParser.getThumbnailsURLs().get(0));


    }

    private static void download(IWebCrawler webCrawler, String painter, String painting) throws IOException {
        ArrayList<String> secondaryKeywords = new ArrayList<>();
        secondaryKeywords.addAll(Arrays.asList(painter.split(" ")));

        InputStream d = webCrawler.downloadImages(30, painting, secondaryKeywords);
        BufferedReader br = new BufferedReader(new InputStreamReader(d));
        String line;

        while((line = br.readLine()) != null){
            System.out.println(line);
        }

        d = webCrawler.filterImages(painting);
        br = new BufferedReader(new InputStreamReader(d));

        while((line = br.readLine()) != null){
            System.out.println(line);
        }

        d = webCrawler.resizeImages(painting);
        br = new BufferedReader(new InputStreamReader(d));

        while((line = br.readLine()) != null){
            System.out.println(line);
        }
    }

    private static void trainModels(ArrayList<String> paintings) throws IOException {

        HashMap<String, String> negativeSamplesPerPaint = new HashMap<>();

        for(String p : paintings){
            long quote = 5000000; //5 MB

            String path = "res/images/"+p.replace(' ','_')+"/";
            List<String> results = new ArrayList<>();

            FileOutputStream fos = new FileOutputStream(path+p.replace(' ','_')+".zip");
            ZipOutputStream zos = new ZipOutputStream(fos);

            File[] files = new File(path).listFiles();
            //If this pathname does not denote a directory, then listFiles() returns null.

            for (File file : files) {
                if (file.isFile() && !file.getName().contains(".zip")){
                    results.add(file.getName());
                    if(quote - file.length() > 0){
                        addToZipFile(path+file.getName(), zos);
                        quote -= file.length();
                    }
                }


            }

            zos.close();
            fos.close();

            StringBuilder stringBuilder = new StringBuilder();

            for(int i = 0; i < 20 && i < results.size(); i++)
                stringBuilder.append(results.get(i)).append(" ");

            negativeSamplesPerPaint.put(path, stringBuilder.toString());

        }

        for (String painting : paintings) {

            IIBMCustomClassifierUtility customClassifierUtility = new IBMCustomClassifierUtility();
            //compression of negative samples

            String pathWithoutSpaces = "res/images/" + painting.replace(' ', '_')+"/";

            String zipName = pathWithoutSpaces + painting.replace(" ","_") +
                    "_negative_samples.zip";

            FileOutputStream fos = new FileOutputStream(zipName);
            ZipOutputStream zos = new ZipOutputStream(fos);

            for(Map.Entry<String, String> entry : negativeSamplesPerPaint.entrySet()){
                if(!entry.getKey().equals(pathWithoutSpaces))
                    for(String s : entry.getValue().split(" "))
                        addToZipFile(entry.getKey()+s,zos);
            }

            zos.close();
            fos.close();

            HashMap<String, String> positiveSamples = new HashMap<>();
            ArrayList<String> negativeSamples = new ArrayList<>();

            String name;
            if(painting.length() > 49)
                name = painting.substring(0,49).replace("'","");
            else
                name = painting.replace("'","");
            positiveSamples.put(name, zipName.replace("_negative_samples",""));

            negativeSamples.add(zipName);

            Classifier vc = customClassifierUtility.createClassifier(name, positiveSamples, negativeSamples);
        }
    }

    private static void addToZipFile(String fileName, ZipOutputStream zos) throws FileNotFoundException, IOException {

        System.out.println("Writing '" + fileName + "' to zip file");

        File file = new File(fileName);
        FileInputStream fis = new FileInputStream(file);
        ZipEntry zipEntry = new ZipEntry(fileName);
        zos.putNextEntry(zipEntry);

        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zos.write(bytes, 0, length);
        }

        zos.closeEntry();
        fis.close();
    }

    private static void saveResultsInDB(ArrayList<String> paintings, ArrayList<String> creator,
                                        ArrayList<String> descriptions, ArrayList<String> thumbnailsURL,
                                        ArrayList<String> museums, ArrayList<String> keywords) throws FileNotFoundException {

        String divider = "\t";
        String header = "id"+divider+"painting"+divider+"author"+divider+"description"+divider+
                "thumbnailURL"+divider+"museum"+divider+"keywords\n";
        PrintWriter pw = new PrintWriter(new File("res/smartdb.csv"));
        StringBuilder sb = new StringBuilder();
        sb.append(header);


        for(int i = 0; i<paintings.size(); i++){
            sb.append(paintings.get(i).replace(" ","_").replace(",",""));
            sb.append(divider);
            sb.append(paintings.get(i));
            sb.append(divider);
            sb.append(creator.get(i));
            sb.append(divider);
            sb.append(descriptions.get(i).replace("\n",""));
            sb.append(divider);
            sb.append(thumbnailsURL.get(i));
            sb.append(divider);
            sb.append(museums.get(i));
            sb.append(divider);
            sb.append(keywords.get(i));
            sb.append("\n");
        }

        pw.write(sb.toString());
        pw.close();
        System.out.println("done!");
    }


}

