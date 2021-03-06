package com.tfederico.pearlBackend.db;

import com.tfederico.pearlBackend.db.contract.IPaintingDbReader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class PaintingDbReader implements IPaintingDbReader{

    private ArrayList<String> museums;
    private ArrayList<String> painters;
    private ArrayList<String> paintings;
    private String csvFile = "res/database.csv";
    private BufferedReader br = null;
    private FileReader fileReader = null;
    private static String line = "";
    private static String cvsSplitBy = "\t";

    public PaintingDbReader(){
        museums = new ArrayList<>();
        painters = new ArrayList<>();
        paintings = new ArrayList<>();
    }

    /**
     * Method used to open the csv file used as database
     * @throws FileNotFoundException
     */
    @Override
    public void openDatabase() throws FileNotFoundException {

        fileReader = new FileReader(csvFile);
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
            museums.add(data[0]);
            painters.add(data[1]);
            paintings.add(data[2]);

        }

        br.close();

        museums.remove(0);
        painters.remove(0);
        paintings.remove(0);
    }

    /**
     * Method used to retrieve the name of the paintings in the database
     * @return list of paintings names
     */
    @Override
    public ArrayList<String> getPaintings() {
        return paintings;
    }

    /**
     * Method used to retrieve the name of the authors in the database
     * @return list of authors' names
     */
    @Override
    public ArrayList<String> getAuthors() {
        return painters;
    }

    /**
     * Method used to retrieve the name of the museums in the database
     * @return list of museums names
     */
    @Override
    public ArrayList<String> getMuseums() {
        return museums;
    }

    /**
     * Method used to open the csv file used as database
     * @throws FileNotFoundException
     */
    @Override
    public void closeDatabase() throws IOException {
        fileReader.close();
    }


}
