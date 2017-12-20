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

public class Benchmarker implements IBenchmarker {
    @Override
    public void benchmark(String path) throws IOException {
        File folder = new File(path);

        int counter = 0;
        ArrayList<Integer> positives = new ArrayList<>();
        ArrayList<String> mismatches = new ArrayList<>();
        ArrayList<String> paintings = new ArrayList<>();

        for (int i = 0; i < folder.listFiles().length; i++){
            File subfolder = folder.listFiles()[i];
            IIBMCustomClassifierUtility customClassifier = new IBMCustomClassifierUtility();
            File f = new File(path+"/"+subfolder.getName());
            Runtime.getRuntime().exec("python3 resizeBenchmark.py --d "+subfolder.getName());
            int pos = 0;
            String painting = subfolder.getName().replace("_", " ");
            System.out.println("########## PAINTING "+i+" ##########");
            System.out.println(painting);
            paintings.add(painting);
            for (File img : f.listFiles()){
                System.out.println(img.getName());
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
                    pos++;
                else
                    mismatches.add(painting+"_"+bestLabel);
            }
            positives.add(pos);
            System.out.println("Painting: "+painting);
            System.out.println("Score: "+positives.get(counter)+"/5");
            counter++;
            customClassifier = null;
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(i % 5 == 0){
                try {
                    Thread.sleep(20000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        IDBWriter dbWriter = new DBWriter();

        dbWriter.saveBenchmark(paintings, positives);
        dbWriter.saveWrongPredictions(mismatches);
    }
}
