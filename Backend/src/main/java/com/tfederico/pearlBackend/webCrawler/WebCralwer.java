package com.tfederico.pearlBackend.webCrawler;

import com.tfederico.pearlBackend.webCrawler.contract.IImageDownloader;
import com.tfederico.pearlBackend.webCrawler.contract.IImageFilterer;
import com.tfederico.pearlBackend.webCrawler.contract.IWebCrawler;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class WebCralwer implements IWebCrawler {

    private IImageDownloader imageDownloader;

    private IImageFilterer imageFilterer;

    private static String pyPath = "python/";

    public WebCralwer(){
        imageDownloader = new ImageDownloader();

        imageFilterer = new ImageFilterer();
    }

    public void downloadImages(int imagesNumber, String paintingName, ArrayList<String> keywords) throws IOException {
        imageDownloader.downloadImages(pyPath, imagesNumber, paintingName, keywords);
    }

    public void filterImages(String dirName) throws IOException {
        imageFilterer.filterImages(pyPath, dirName);
    }
}
