package com.tfederico.pearlBackend.webCrawler.contract;


import java.io.IOException;
import java.io.InputStream;

public interface IImageFilterer {
    InputStream filterImages(String dirName) throws IOException;
}
