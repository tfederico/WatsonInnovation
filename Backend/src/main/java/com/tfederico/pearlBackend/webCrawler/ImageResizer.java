package com.tfederico.pearlBackend.webCrawler;

import com.tfederico.pearlBackend.webCrawler.contract.IImageResizer;

import java.io.IOException;
import java.io.InputStream;

public class ImageResizer implements IImageResizer{

    private static String scriptName = "resize.py";
    @Override
    public InputStream resizeImages(String dirName) throws IOException {
        Process p = Runtime.getRuntime().exec("python3 "+scriptName+" --d "+dirName.replace(' ','_'));
        return p.getInputStream();
    }
}
