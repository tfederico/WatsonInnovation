package com.tfederico.pearlBackend.webCrawler.contract;

import java.io.IOException;
import java.io.InputStream;

public interface IImageResizer {
    /**
     * Method used to resize the downloaded images
     * @param dirName directory containing the images
     * @return an input stream connected to the python script used to resize the images
     * @throws IOException
     */
    InputStream resizeImages(String dirName) throws IOException;
}
