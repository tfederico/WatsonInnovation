package com.tfederico.pearlBackend.db.contract;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.zip.ZipOutputStream;

public interface IZipper {
    void addToZipFile(String fileName, ZipOutputStream zos) throws FileNotFoundException, IOException;
}
