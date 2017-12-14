package com.tfederico.pearlBackend.webCrawler;

import com.tfederico.pearlBackend.webCrawler.contract.IImageFilterer;

import java.io.IOException;

public class ImageFilterer implements IImageFilterer{

    private static String scriptName = "clean.py";

    public String filterImages(String pyPath, String dirName) throws IOException {
        Process p = Runtime.getRuntime().exec("python "+pyPath+scriptName+" --d "+dirName);
        return p.getOutputStream().toString();
    }
}
