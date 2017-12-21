package com.tfederico.pearlBackend.webCrawler.contract;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public interface IImageDownloader {

    /**
     * Method used to download images from google
     * @param imagesNumber number of images (for each keyword) that will be downloaded
     * @param paintingName name of the painting
     * @param keywords additional keywords
     * @return an input stream connected to the python script used to download the images
     * @throws IOException
     */
    InputStream downloadImages(int imagesNumber, String paintingName, ArrayList<String> keywords) throws IOException;
}
