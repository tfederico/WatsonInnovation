package com.tfederico.pearlBackend.watson.visualRecognition;

import com.ibm.watson.developer_cloud.visual_recognition.v3.model.Classifier;
import com.tfederico.libris.image.ibm.contract.IIBMCustomClassifierUtility;
import com.tfederico.libris.image.ibm.visualRecognition.IBMCustomClassifierUtility;
import com.tfederico.pearlBackend.db.Zipper;
import com.tfederico.pearlBackend.db.contract.IZipper;
import com.tfederico.pearlBackend.watson.visualRecognition.contract.IModelTrainer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

public class ModelTrainer implements IModelTrainer{

    private IZipper zipper;

    public ModelTrainer(){
        zipper = new Zipper();
    }

    @Override
    public void trainModels(ArrayList<String> paintings) throws IOException {
        HashMap<String, String> negativeSamplesPerPaint = new HashMap<>();

        //creating zip files for the positive samples
        for(String p : paintings){
            long quote = 5000000; //5 MB

            String path = "res/images/"+p.replace(' ','_')+"/";
            List<String> results = new ArrayList<>();

            FileOutputStream fos = new FileOutputStream(path+p.replace(' ','_')+".zip");
            ZipOutputStream zos = new ZipOutputStream(fos);

            File[] files = new File(path).listFiles();
            //If this pathname does not denote a directory, then listFiles() returns null.

            for (File file : files)
                if (file.isFile() && !file.getName().contains(".zip")){
                    results.add(file.getName());
                    if(quote - file.length() > 0){
                        zipper.addToZipFile(path+file.getName(), zos);
                        quote -= file.length();
                    }
                }

            zos.close();
            fos.close();

            StringBuilder stringBuilder = new StringBuilder();

            for(int i = 0; i < 20 && i < results.size(); i++)
                stringBuilder.append(results.get(i)).append(" ");

            negativeSamplesPerPaint.put(path, stringBuilder.toString());

        }

        //creating zip files for the negative samples and uploading
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
                        zipper.addToZipFile(entry.getKey()+s,zos);
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
}
