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

    @Override
    public ArrayList<String> getQueries() {
        return queries;
    }

    @Override
    public ArrayList<String> getQueriesIds() {
        return queriesIds;
    }

    @Override
    public void openDatabase() throws FileNotFoundException {
        fileReader = new FileReader(csvFile);
    }

    @Override
    public void closeDatabase() throws IOException {
        fileReader.close();
    }
}
