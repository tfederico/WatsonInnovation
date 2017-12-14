package com.tfederico.pearlBackend.webCrawler;

import com.tfederico.pearlBackend.webCrawler.contract.IImageDownloader;

import java.io.IOException;
import java.util.ArrayList;

public class ImageDownloader implements IImageDownloader {

    private static String scriptName = "download.py";

    public String downloadImages(String pyPath, int imagesNumber, String paintingName,
                               ArrayList<String> keywords) throws IOException {

        String keys = "";

        for(String k : keywords){
            keys += k + " ";
        }

        Process p = Runtime.getRuntime().exec("python " + pyPath + scriptName
                + " --n " + imagesNumber + " --p \"" + paintingName + "\" --a \""+ keys +"\"");

        return p.getOutputStream().toString();

    }
}
