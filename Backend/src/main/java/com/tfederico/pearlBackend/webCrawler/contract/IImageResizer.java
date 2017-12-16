package com.tfederico.pearlBackend.webCrawler.contract;

import java.io.IOException;
import java.io.InputStream;

public interface IImageResizer {
    InputStream resizeImages(String dirName) throws IOException;
}
