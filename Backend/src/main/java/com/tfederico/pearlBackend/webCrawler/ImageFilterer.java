package com.tfederico.pearlBackend.webCrawler;

import com.tfederico.pearlBackend.webCrawler.contract.IImageFilterer;
import jdk.internal.util.xml.impl.Input;

import java.io.IOException;
import java.io.InputStream;

public class ImageFilterer implements IImageFilterer{

    private static String scriptName = "clean.py";

    public InputStream filterImages(String dirName) throws IOException {
        Process p = Runtime.getRuntime().exec("python3 "+scriptName+" --d "+dirName.replace(' ','_'));
        return p.getInputStream();
    }
}
