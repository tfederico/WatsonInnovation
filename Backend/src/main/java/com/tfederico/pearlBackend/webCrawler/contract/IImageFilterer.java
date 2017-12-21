package com.tfederico.pearlBackend.webCrawler.contract;


import java.io.IOException;
import java.io.InputStream;

public interface IImageFilterer {

    /**
     * Method used to filter the downloaded images
     * @param dirName name of the directory containing the images
     * @return an input stream connected to the python script used to filter the images
     * @throws IOException
     */
    InputStream filterImages(String dirName) throws IOException;
}
