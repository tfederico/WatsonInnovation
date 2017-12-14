package com.tfederico.pearlBackend.webCrawler.contract;

import java.io.IOException;

public interface IImageFilterer {
    public String filterImages(String pyPath, String dirName) throws IOException;
}
