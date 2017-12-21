package com.tfederico.pearlBackend.db.contract;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public interface IPaintingDbReader {

    /**
     * Method used to read the data contained in the database
     * @throws IOException
     */
    void readDatabase() throws IOException;

    /**
     * Method used to retrieve the name of the paintings in the database
     * @return list of paintings names
     */
    ArrayList<String> getPaintings();

    /**
     * Method used to retrieve the name of the authors in the database
     * @return list of authors' names
     */
    ArrayList<String> getAuthors();

    /**
     * Method used to retrieve the name of the paintings in the database
     * @return list of museums names
     */
    ArrayList<String> getMuseums();

    /**
     * Method used to open the csv file used as database
     * @throws FileNotFoundException
     */
    void openDatabase() throws FileNotFoundException;

    /**
     * Method used to close the csv file used as database
     * @throws FileNotFoundException
     */
    void closeDatabase() throws IOException;
}
