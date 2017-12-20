package com.tfederico.pearlBackend.db.contract;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public interface IDBWriter {

    void saveItemSimilarities(ArrayList<String> paintings, Integer[][] matrix,
                              List<Set<String>> keys) throws FileNotFoundException;

    void saveBenchmark(ArrayList<String> paintings, ArrayList<Integer> score) throws FileNotFoundException;

    void saveWrongPredictions(ArrayList<String> predictions) throws FileNotFoundException;

    void createSmartDB(ArrayList<String> paintings, ArrayList<String> creator,
                       ArrayList<String> descriptions, ArrayList<String> thumbnailsURL,
                       ArrayList<String> museums, ArrayList<String> keywords) throws FileNotFoundException;
}
