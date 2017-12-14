package com.tfederico.pearlBackend.webCrawler.contract;

import java.io.IOException;
import java.util.ArrayList;

public interface IImageDownloader {
    public String downloadImages(String pyPath, int imagesNumber, String paintingName, ArrayList<String> keywords) throws IOException;
}
