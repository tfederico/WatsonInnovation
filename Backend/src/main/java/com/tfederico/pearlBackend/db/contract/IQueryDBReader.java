package com.tfederico.pearlBackend.db.contract;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public interface IQueryDBReader {

    /**
     * Method used to read the data contained in the database
     * @throws IOException
     */
    void readDatabase() throws IOException;

    /**
     * Method used to retrieve the queries in the database
     * @return list of queries
     */
    ArrayList<String> getQueries();

    /**
     * Method used to open the file used as database
     * @throws FileNotFoundException
     */
    void openDatabase() throws FileNotFoundException;

    /**
     * Method used to close the file used as database
     * @throws FileNotFoundException
     */
    void closeDatabase() throws IOException;

    /**
     * Method used to retrieve the unique identifiers of the queries in the database
     * @return list of unique identifiers
     */
    ArrayList<String> getQueriesIds();
}
