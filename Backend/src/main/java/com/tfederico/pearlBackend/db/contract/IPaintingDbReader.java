package com.tfederico.pearlBackend.db.contract;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public interface IPaintingDbReader {

    void readDatabase() throws IOException;

    ArrayList<String> getPaintings();

    ArrayList<String> getAuthors();

    ArrayList<String> getMuseums();

    void openDatabase() throws FileNotFoundException;
}
