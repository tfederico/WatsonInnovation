package com.tfederico.pearlBackend.webCrawler;

import com.tfederico.pearlBackend.webCrawler.contract.IImageDownloader;
import com.tfederico.pearlBackend.webCrawler.contract.IImageFilterer;
import com.tfederico.pearlBackend.webCrawler.contract.IImageResizer;
import com.tfederico.pearlBackend.webCrawler.contract.IWebCrawler;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

public class WebCralwer implements IWebCrawler {

    private IImageDownloader imageDownloader;

    private IImageFilterer imageFilterer;

    private IImageResizer imageResizer;

    public WebCralwer(){
        imageDownloader = new ImageDownloader();

        imageFilterer = new ImageFilterer();

        imageResizer = new ImageResizer();
    }


    private InputStream downloadImages(int imagesNumber, String paintingName, ArrayList<String> keywords) throws IOException {
        return imageDownloader.downloadImages(imagesNumber, paintingName, keywords);
    }


    private InputStream resizeImages(String dirName) throws IOException {
        return imageResizer.resizeImages(dirName);
    }

    /**
     * Method used to download images from Google in order to train the Visual Recognition
     * service
     * @param painter name and surname of the painter
     * @param painting name of the painting
     * @throws IOException
     */
    @Override
    public void download(String painter, String painting) throws IOException {
        ArrayList<String> secondaryKeywords = new ArrayList<>();
        secondaryKeywords.addAll(Arrays.asList(painter.split(" ")));

        InputStream d = downloadImages(30, painting, secondaryKeywords);
        BufferedReader br = new BufferedReader(new InputStreamReader(d));
        String line;

        while((line = br.readLine()) != null){
            System.out.println(line);
        }

        d = filterImages(painting);
        br = new BufferedReader(new InputStreamReader(d));

        while((line = br.readLine()) != null){
            System.out.println(line);
        }

        d = resizeImages(painting);
        br = new BufferedReader(new InputStreamReader(d));

        while((line = br.readLine()) != null){
            System.out.println(line);
        }
    }

    private InputStream filterImages(String dirName) throws IOException {
        return imageFilterer.filterImages(dirName);
    }
}
