package com.tfederico.pearlBackend.db;

import com.tfederico.pearlBackend.db.contract.IQueryDBReader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class QueryDBReader implements IQueryDBReader {

    private ArrayList<String> queries;
    private ArrayList<String> queriesIds;

    private String csvFile = "res/queries.csv";
    private BufferedReader br = null;
    private FileReader fileReader = null;
    private static String line = "";
    private static String cvsSplitBy = "\t";

    public QueryDBReader(){
        queries = new ArrayList<>();
        queriesIds = new ArrayList<>();
    }

    /**
     * Method used to read the data contained in the database
     * @throws IOException
     */
    @Override
    public void readDatabase() throws IOException {
        br = new BufferedReader(fileReader);
        while ((line = br.readLine()) != null) {

            // use semi-colon as separator
            String[] data = line.split(cvsSplitBy);
            queriesIds.add(data[0]);
            queries.add(data[1]);

        }

        br.close();

        queries.remove(0);
        queriesIds.remove(0);
    }

    /**
     * Method used to retrieve the queries in the database
     * @return list of queries
     */
    @Override
    public ArrayList<String> getQueries() {
        return queries;
    }

    /**
     * Method used to retrieve the unique identifiers of the queries in the database
     * @return list of unique identifiers
     */
    @Override
    public ArrayList<String> getQueriesIds() {
        return queriesIds;
    }

    /**
     * Method used to open the file used as database
     * @throws FileNotFoundException
     */
    @Override
    public void openDatabase() throws FileNotFoundException {
        fileReader = new FileReader(csvFile);
    }

    /**
     * Method used to close the file used as database
     * @throws FileNotFoundException
     */
    @Override
    public void closeDatabase() throws IOException {
        fileReader.close();
    }
}
