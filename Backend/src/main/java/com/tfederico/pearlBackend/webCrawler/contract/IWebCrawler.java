package com.tfederico.pearlBackend.webCrawler.contract;

import java.io.IOException;

public interface IWebCrawler {
    public void filterImages(String dirName) throws IOException;
}
