package com.tfederico.pearlBackend.webCrawler;

import com.tfederico.pearlBackend.webCrawler.contract.IImageDownloader;
import com.tfederico.pearlBackend.webCrawler.contract.IImageFilterer;
import com.tfederico.pearlBackend.webCrawler.contract.IWebCrawler;

import java.awt.*;

public class WebCralwer implements IWebCrawler {

    private IImageDownloader imageDownloader;

    private IImageFilterer imageFilterer;

    public WebCralwer(){
        imageDownloader = new ImageDownloader();

        imageFilterer = new ImageFilterer();
    }
}
