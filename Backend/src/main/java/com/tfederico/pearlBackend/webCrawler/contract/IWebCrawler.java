package com.tfederico.pearlBackend.webCrawler.contract;

import jdk.internal.util.xml.impl.Input;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public interface IWebCrawler {
    InputStream filterImages(String dirName) throws IOException;
    InputStream downloadImages(int imagesNumber, String paintingName, ArrayList<String> keywords) throws IOException;
}
