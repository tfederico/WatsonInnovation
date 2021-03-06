package com.tfederico.pearlBackend.db;

import com.tfederico.pearlBackend.db.contract.IZipper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Zipper implements IZipper {

    /**
     * Method used to add a file to a zip
     * @param fileName name of the file to add
     * @param zos object used to write files in a zip
     * @throws FileNotFoundException
     * @throws IOException
     */
    @Override
    public void addToZipFile(String fileName, ZipOutputStream zos) throws IOException {
        //System.out.println("Writing '" + fileName + "' to zip file");

        File file = new File(fileName);
        FileInputStream fis = new FileInputStream(file);
        ZipEntry zipEntry = new ZipEntry(fileName);
        zos.putNextEntry(zipEntry);

        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zos.write(bytes, 0, length);
        }

        zos.closeEntry();
        fis.close();
    }
}
