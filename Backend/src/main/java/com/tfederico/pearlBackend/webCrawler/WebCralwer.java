package com.tfederico.pearlBackend.webCrawler;

import com.tfederico.pearlBackend.webCrawler.contract.IImageDownloader;
import com.tfederico.pearlBackend.webCrawler.contract.IImageFilterer;
import com.tfederico.pearlBackend.webCrawler.contract.IWebCrawler;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class WebCralwer implements IWebCrawler {

    private IImageDownloader imageDownloader;

    private IImageFilterer imageFilterer;

    public WebCralwer(){
        imageDownloader = new ImageDownloader();

        imageFilterer = new ImageFilterer();
    }

    public InputStream downloadImages(int imagesNumber, String paintingName, ArrayList<String> keywords) throws IOException {
        return imageDownloader.downloadImages(imagesNumber, paintingName, keywords);
    }

    public InputStream filterImages(String dirName) throws IOException {
        return imageFilterer.filterImages(dirName);
    }
}
