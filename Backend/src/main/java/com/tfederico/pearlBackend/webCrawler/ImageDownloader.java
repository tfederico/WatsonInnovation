package com.tfederico.pearlBackend.webCrawler;

import com.tfederico.pearlBackend.webCrawler.contract.IImageDownloader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ImageDownloader implements IImageDownloader {

    private static String scriptName = "download.py";

    public InputStream downloadImages(int imagesNumber, String paintingName,
                                      ArrayList<String> keywords) throws IOException {

        StringBuilder keys = new StringBuilder();

        for(String k : keywords){
            keys.append(k).append("_");
        }

        String k = keys.substring(keys.length()-1);

        Process p = Runtime.getRuntime().exec("python3 " + scriptName
                + " --n " + imagesNumber + " --p " + paintingName.replace(' ','_')
                + " --a "+k);

        return p.getInputStream();

    }
}
