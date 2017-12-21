package com.tfederico.pearlBackend.db.contract;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.zip.ZipOutputStream;

public interface IZipper {
    /**
     * Method used to add a file to a zip
     * @param fileName name of the file to add
     * @param zos object used to write files in a zip
     * @throws FileNotFoundException
     * @throws IOException
     */
    void addToZipFile(String fileName, ZipOutputStream zos) throws IOException;
}
