package com.tfederico.pearlBackend.webCrawler.contract;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public interface IImageDownloader {
    InputStream downloadImages(int imagesNumber, String paintingName, ArrayList<String> keywords) throws IOException;
}
