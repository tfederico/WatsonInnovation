package com.tfederico.pearlBackend.benchmarking;

import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassResult;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifiedImage;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifiedImages;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifierResult;
import com.tfederico.libris.image.ibm.contract.IIBMCustomClassifierUtility;
import com.tfederico.libris.image.ibm.visualRecognition.IBMCustomClassifierUtility;
import com.tfederico.pearlBackend.benchmarking.contract.IBenchmarker;
import com.tfederico.pearlBackend.db.DBWriter;
import com.tfederico.pearlBackend.db.contract.IDBWriter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;



public class Benchmarker implements IBenchmarker {

    /**
     * Method used to evaluate the goodness of the classifiers
     * @param path path to the folder containing the images that have to be classified
     * @throws IOException
     */
    @Override
    public void benchmark(String path) throws IOException {


        File folder = new File(path);
        System.out.println("Select the folder from which images will be classified:");
        for(int i = 0; i < folder.listFiles().length; i++){
           System.out.println(i+") "+folder.listFiles()[i].getName());
        }

        Scanner scanner = new Scanner(System.in);
        int index = Integer.valueOf(scanner.next());

        ArrayList<String> mismatches = new ArrayList<>();
        File subfolder = folder.listFiles()[index];

        IIBMCustomClassifierUtility customClassifier = new IBMCustomClassifierUtility();

        File f = new File(path+"/"+subfolder.getName());
        Runtime.getRuntime().exec("python3 resizeBenchmark.py --d "+subfolder.getName());
        int positives = 0;
        String painting = subfolder.getName().replace("_", " ");

        for (File img : f.listFiles()){

            //System.out.println(img.getName());
            Float bestScore = -1.0f;
            String bestLabel = "";
            ClassifiedImages images = customClassifier.classify(img.getPath());

            for(ClassifiedImage cImg : images.getImages()) {
                for (ClassifierResult c : cImg.getClassifiers())
                    for (ClassResult cr : c.getClasses())
                        if (cr.getScore() > bestScore) {
                            bestScore = cr.getScore();
                            bestLabel = cr.getClassName();
                        }
            }
            bestLabel = bestLabel.replace("_", " ");
            System.out.println(bestLabel);
            String shortPaint;
            if (painting.length() > 49)
                shortPaint = painting.replace("'","").toLowerCase().substring(0,49);
            else
                shortPaint = painting.replace("'","").toLowerCase();

            if(bestLabel.toLowerCase().equals(shortPaint))
                positives++;
            else
                mismatches.add(painting+"_"+bestLabel);
        }

        System.out.println("Painting: "+painting);
        System.out.println("Score: "+positives+"/5");



        IDBWriter dbWriter = new DBWriter();

        dbWriter.saveBenchmark(painting, positives);
        dbWriter.saveWrongPredictions(mismatches);
    }
}
