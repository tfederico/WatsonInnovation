package com.tfederico.pearlBackend.webCrawler;

import com.tfederico.pearlBackend.webCrawler.contract.IImageFilterer;
import jdk.internal.util.xml.impl.Input;

import java.io.IOException;
import java.io.InputStream;

public class ImageFilterer implements IImageFilterer{

    private static String scriptName = "clean.py";

    /**
     * Method used to filter the downloaded images
     * @param dirName name of the directory containing the images
     * @return an input stream connected to the python script used to filter the images
     * @throws IOException
     */
    @Override
    public InputStream filterImages(String dirName) throws IOException {
        Process p = Runtime.getRuntime().exec("python3 "+scriptName+" --d "+dirName.replace(' ','_'));
        return p.getInputStream();
    }
}
