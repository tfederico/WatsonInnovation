package com.tfederico.pearlBackend.db.contract;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public interface IQueryDBReader {

    void readDatabase() throws IOException;

    ArrayList<String> getQueries();

    void openDatabase() throws FileNotFoundException;

    void closeDatabase() throws IOException;

    ArrayList<String> getQueriesIds();
}
