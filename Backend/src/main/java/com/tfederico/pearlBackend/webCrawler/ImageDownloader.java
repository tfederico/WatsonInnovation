package com.tfederico.pearlBackend.webCrawler;

import com.tfederico.pearlBackend.webCrawler.contract.IImageDownloader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ImageDownloader implements IImageDownloader {

    private static String scriptName = "download.py";

    /**
     * Method used to download images from google
     * @param imagesNumber number of images (for each keyword) that will be downloaded
     * @param paintingName name of the painting
     * @param keywords additional keywords
     * @return an input stream connected to the python script used to download the images
     * @throws IOException
     */
    public InputStream downloadImages(int imagesNumber, String paintingName,
                                      ArrayList<String> keywords) throws IOException {

        StringBuilder keys = new StringBuilder();

        for(String k : keywords){
            keys.append(k).append("_");
        }

        String k = keys.substring(keys.length()-1);

        Process p = Runtime.getRuntime().exec("python " + scriptName
                + " --n " + imagesNumber + " --p " + paintingName.replace(' ','_')
                + " --a "+k);

        return p.getInputStream();

    }
}
