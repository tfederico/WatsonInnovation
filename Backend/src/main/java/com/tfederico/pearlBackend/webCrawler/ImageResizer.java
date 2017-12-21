package com.tfederico.pearlBackend.webCrawler;

import com.tfederico.pearlBackend.webCrawler.contract.IImageResizer;

import java.io.IOException;
import java.io.InputStream;

public class ImageResizer implements IImageResizer{

    private static String scriptName = "resize.py";

    /**
     * Method used to resize the downloaded images
     * @param dirName directory containing the images
     * @return an input stream connected to the python script used to resize the images
     * @throws IOException
     */
    @Override
    public InputStream resizeImages(String dirName) throws IOException {
        Process p = Runtime.getRuntime().exec("python3 "+scriptName+" --d "+dirName.replace(' ','_'));
        return p.getInputStream();
    }
}
