package com.tfederico.pearlBackend.webCrawler.contract;

import jdk.internal.util.xml.impl.Input;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public interface IWebCrawler {
    void download(String painter, String painting) throws IOException;

}
