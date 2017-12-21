package com.tfederico.pearlBackend;

import com.ibm.watson.developer_cloud.discovery.v1.model.QueryResult;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.Classifier;
import com.tfederico.libris.image.ibm.contract.IIBMCustomClassifierUtility;
import com.tfederico.libris.image.ibm.visualRecognition.IBMCustomClassifierUtility;
import com.tfederico.libris.text.discovery.ibm.IBMDiscoveryUtility;
import com.tfederico.libris.text.naturalLanguageUnderstanding.ibm.IBMNaturalLanguageUnderstandingUtility;
import com.tfederico.pearlBackend.benchmarking.Benchmarker;
import com.tfederico.pearlBackend.benchmarking.contract.IBenchmarker;
import com.tfederico.pearlBackend.db.DBWriter;
import com.tfederico.pearlBackend.db.PaintingDbReader;
import com.tfederico.pearlBackend.db.QueryDBReader;
import com.tfederico.pearlBackend.db.contract.IDBWriter;
import com.tfederico.pearlBackend.db.contract.IPaintingDbReader;
import com.tfederico.pearlBackend.db.contract.IQueryDBReader;
import com.tfederico.pearlBackend.europeana.EuropeanaUtility;
import com.tfederico.pearlBackend.europeana.contract.IEuropeanaUtility;
import com.tfederico.pearlBackend.exceptions.UnknownMuseumException;
import com.tfederico.pearlBackend.watson.discovery.DiscoveryUtility;
import com.tfederico.pearlBackend.watson.discovery.contract.IDiscoveryUtility;
import com.tfederico.pearlBackend.watson.nlu.NLUUtility;
import com.tfederico.pearlBackend.watson.nlu.contract.INLUUtility;
import com.tfederico.pearlBackend.watson.visualRecognition.ModelTrainer;
import com.tfederico.pearlBackend.watson.visualRecognition.contract.IModelTrainer;
import com.tfederico.pearlBackend.webCrawler.WebCralwer;
import com.tfederico.pearlBackend.webCrawler.contract.IWebCrawler;
import eu.europeana.api.client.exception.EuropeanaApiProblem;
import java.io.*;
import java.util.*;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException, UnknownMuseumException, EuropeanaApiProblem {

        setCredentials("credentials.csv");

        Scanner scanner = new Scanner(System.in);
        System.out.println("Do you want to train (t) or benchmark (b)?");
        String trainingOrBenchmarking = scanner.next().toLowerCase();
        long start = System.currentTimeMillis();

        if(trainingOrBenchmarking.equals("t") || trainingOrBenchmarking.equals("train")){


            System.out.println("Do you want to delete pre-existing models? (y/n)");
            String delete = scanner.next().toLowerCase();

            if(delete.equals("y")){
                System.out.println("Deleting models...");
                IIBMCustomClassifierUtility cu = new IBMCustomClassifierUtility();
                List<Classifier> classifierList = cu.getClassifiersList();

                if(cu.getClassifiersList().size() != 0){
                    for(Classifier c : classifierList){
                        cu.deleteClassifier(c.getClassifierId());
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
            //pre-existing data
            IPaintingDbReader paintingDbReader = new PaintingDbReader();
            paintingDbReader.openDatabase();
            paintingDbReader.readDatabase();
            ArrayList<String> paintings = paintingDbReader.getPaintings();
            ArrayList<String> painters = paintingDbReader.getAuthors();
            ArrayList<String> museums = paintingDbReader.getMuseums();
            paintingDbReader.closeDatabase();

            //data to retrieve from europeana
            ArrayList<String> descriptions = new ArrayList<>();
            ArrayList<String> creator = new ArrayList<>(); //problems with europeana
            ArrayList<String> thumbnailsURL = new ArrayList<>();


            IEuropeanaUtility europeanaUtility = new EuropeanaUtility();
            IWebCrawler webCrawler = new WebCralwer();

            System.out.println("################ DOWNLOADING IMAGES ################");
            for(int i = 0; i < paintings.size(); i++){
                System.out.println("########### PAINTING "+(i+1)+"/"+paintings.size()+" ##########");
                europeanaUtility.callEuropeana(museums.get(i), paintings.get(i), descriptions,
                        creator, thumbnailsURL);
                webCrawler.download(painters.get(i), paintings.get(i));
            }

            System.out.println("################ MODELS TRAINING ################");
            IModelTrainer modelTrainer = new ModelTrainer();
            modelTrainer.trainModels(paintings);

            ArrayList<String> keyword;
            IDBWriter dbWriter = new DBWriter();
            System.out.println("################ EXTRACTING KEYWORDS ################");
            INLUUtility nluUtility = new NLUUtility();

            keyword = nluUtility.getKeywordsFromTexts(descriptions);

            System.out.println("################ SMARTDB BUILDING ################");

            dbWriter.createSmartDB(paintings, painters, descriptions, thumbnailsURL, museums, keyword);
            Runtime.getRuntime().exec("python3 toJSON.py");

            System.out.println("################ CALCULATING SIMILARITY ################");
            calculateIntersection("res/smartdb.csv");
        }
        else{

            System.out.println("Select the service: discovery (d), visual recognition (vr) or both (b):");

            String service = scanner.next().toLowerCase();

            if(service.equals("b") || service.equals("both") || service.equals("d") || service.equals("discovery")){
                System.out.println("################ QUERYING DISCOVERY ################");
                IQueryDBReader queryDBReader = new QueryDBReader();
                queryDBReader.openDatabase();
                queryDBReader.readDatabase();
                ArrayList<String> queries = queryDBReader.getQueries();
                ArrayList<String> queriesIds = queryDBReader.getQueriesIds();

                queryDBReader.closeDatabase();

                IDiscoveryUtility discoveryUtility = new DiscoveryUtility();
                IDBWriter dbWriter = new DBWriter();
                for(int i = 0; i < queriesIds.size(); i++){
                    List<QueryResult> results = discoveryUtility.naturalLanguageQuery(queries.get(i));
                    dbWriter.saveQueryResults(queriesIds.get(i),results);
                }
            }

            if(service.equals("b") || service.equals("both") || service.equals("vr") || service.equals("visual recognition")){
                IIBMCustomClassifierUtility u = new IBMCustomClassifierUtility();
                int notReady = 0;
                for (Classifier c : u.getClassifiersList()){
                    if(c.getStatus().equals("training"))
                        notReady++;
                }

                String goOn = "yes";

                while(goOn.equals("yes") || goOn.equals("y")){
                    if(notReady != 0){
                        System.out.println("I am not ready to classify images. There are still "+notReady+" classifiers training");
                        goOn = "no";
                    }
                    else{
                        System.out.println("################ BENCHMARKING ################");
                        IBenchmarker benchmarker = new Benchmarker();
                        benchmarker.benchmark("res/benchmark/");
                        System.out.println("Do you wish to benchmark another collection of images? (y/n)");
                        goOn = scanner.next();
                    }
                }
            }


            System.out.println("Required time: "+(System.currentTimeMillis()-start)+" ms");

        }
    }

    /**
     * Method that set the credentials for the different IBM Bluemix services
     * @param path relative path of the file containing the credentials
     * @throws IOException
     */
    private static void setCredentials(String path) throws IOException {
        FileReader f = new FileReader(path);

        String line;
        BufferedReader br = new BufferedReader(f);
        ArrayList<String> keys = new ArrayList<>();
        while ((line = br.readLine()) != null) {
            // use comma as separator
            String[] data = line.split("\t");
            keys.add(data[1]);
        }

        IBMCustomClassifierUtility.setSubscriptionKey(keys.get(0));
        IBMNaturalLanguageUnderstandingUtility.setUsernameAndPassword(keys.get(1),keys.get(2));
        IBMDiscoveryUtility.setUserAndPassword(keys.get(3),keys.get(4));
        DiscoveryUtility.setCollectionId(keys.get(6));
        DiscoveryUtility.setEnvironmentId(keys.get(5));

        br.close();
    }

    private static void calculateIntersection(String dbPath) throws IOException {
        FileReader f = new FileReader(dbPath);

        String line;
        BufferedReader br = new BufferedReader(f);
        List<Set<String>> keys = new ArrayList<>();
        br.readLine(); //skip header
        ArrayList<String> paintings = new ArrayList<>();
        int i = 0;
        while ((line = br.readLine()) != null) {
            // use tab as separator
            keys.add(new HashSet<String>());
            String[] data = line.split("\t");
            paintings.add(data[1]);
            for(String s : data[6].split(" "))
                keys.get(i).add(s);
            i++;
        }


        br.close();

        Integer[][] matrix = new Integer[keys.size()][keys.size()];
        for(int j = 0; j < keys.size(); j++){
            Set<String> set = keys.get(j);
            //System.out.println("#####################");
            for(int k = j; k<keys.size(); k++){
                Set<String> intersection = new HashSet<>(set);
                intersection.retainAll(keys.get(k));
                //System.out.println(intersection.size());
                matrix[j][k] = intersection.size();
                matrix[k][j] = intersection.size();
            }
        }

        IDBWriter db = new DBWriter();
        db.saveItemSimilarities(paintings, matrix, keys);

    }
}

