package com.tfederico.pearlBackend.webCrawler.contract;

import jdk.internal.util.xml.impl.Input;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public interface IWebCrawler {
    /**
     * Method used to download images from Google in order to train the Visual Recognition
     * service
     * @param painter name and surname of the painter
     * @param painting name of the painting
     * @throws IOException
     */
    void download(String painter, String painting) throws IOException;

}
