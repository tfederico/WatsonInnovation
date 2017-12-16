package com.tfederico.pearlBackend;

import com.ibm.watson.developer_cloud.visual_recognition.v3.model.Classifier;
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
import it.polpetta.libris.image.ibm.contract.IIBMCustomClassifierUtility;
import it.polpetta.libris.image.ibm.visualRecognition.IBMCustomClassifierUtility;

import java.io.*;
import java.util.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {

    public static void main(String[] args) throws IOException, UnknownMuseumException, EuropeanaApiProblem {

        long start = System.currentTimeMillis();

        IPaintingDbReader paintingDbReader = new PaintingDbReader();
        paintingDbReader.openDatabase();
        paintingDbReader.readDatabase();

        ArrayList<String> paintings = paintingDbReader.getPaintings();
        ArrayList<String> painters = paintingDbReader.getAuthors();
        ArrayList<String> museums = paintingDbReader.getMuseums();

        IQuerier querier;
        IQueryBuilder queryBuilder = new QueryBuilder();
        QueryResultParser resultParser;

        EuropeanaApi2Results results;

        ArrayList<String> descriptions = new ArrayList<>();
        ArrayList<String> creator = new ArrayList<>();
        ArrayList<String> thumbnailsURL = new ArrayList<>();
        ArrayList<String> country = new ArrayList<>();

        IWebCrawler webCrawler = new WebCralwer();



        for(int i = 0; i < paintings.size(); i++){

            queryBuilder.createNewQuery();
            queryBuilder.setTitle(paintings.get(i));

            if(Objects.equals(museums.get(i), "Mauritshuis"))
                queryBuilder.setCollectionName("2021672_Ag_NL_DigitaleCollectie_Mauritshuis");
            else if (Objects.equals(museums.get(i), "Rijksmuseum"))
                queryBuilder.setCollectionName("90402_M_NL_Rijksmuseum");
            else
                throw new UnknownMuseumException();

            //queryBuilder.setLanguage("en");
            querier = new Querier();
            results = querier.search(queryBuilder.getQuery(),1,0);
            resultParser = new QueryResultParser(results);

            descriptions.add(resultParser.getDescriptions().get(0));
            creator.add(resultParser.getCreators().get(0));
            thumbnailsURL.add(resultParser.getThumbnailsURLs().get(0));
            country.add(resultParser.getCreators().get(0));

            /*ArrayList<String> secondaryKeywords = new ArrayList<>();
            secondaryKeywords.addAll(Arrays.asList(painters.get(i).split(" ")));

            InputStream d = webCrawler.downloadImages(50, paintings.get(i), secondaryKeywords);
            BufferedReader br = new BufferedReader(new InputStreamReader(d));
            String line;

            while((line = br.readLine()) != null){
                System.out.println(line);
            }

            d = webCrawler.filterImages(new String(paintings.get(i)));
            br = new BufferedReader(new InputStreamReader(d));

            while((line = br.readLine()) != null){
                System.out.println(line);
            }*/
        }


        trainModels(paintings);




        System.out.println("Required time: "+(System.currentTimeMillis()-start)+" ms");

    }

    private static void trainModels(ArrayList<String> paintings) throws IOException {
        IBMCustomClassifierUtility.setSubscriptionKey("");


        ArrayList<String> paintingsDirs = new ArrayList<>();

        HashMap<String, String> negativeSamplesPerPaint = new HashMap<>();



        for(String p : paintings){
            long quote = 5000000; //5 MB

            String path = "res/images/"+p.replace(' ','_')+"/";
            paintingsDirs.add(path);
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

            for(int i = 0; i < 15; i++)
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
}

