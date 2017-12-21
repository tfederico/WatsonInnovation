package com.tfederico.pearlBackend.db.contract;

import com.ibm.watson.developer_cloud.discovery.v1.model.QueryResult;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public interface IDBWriter {

    /**
     *
     * @param paintings names of the paintings
     * @param matrix matrix indicating the number of common keywords between each couple of paintings
     * @param keys list of set of keywords for each painting
     * @throws FileNotFoundException
     */
    void saveItemSimilarities(ArrayList<String> paintings, Integer[][] matrix,
                              List<Set<String>> keys) throws FileNotFoundException;

    /**
     * Method used to save the benchmark result
     * @param painting name of the benchmarked painting
     * @param score number of correct predicitions
     * @throws FileNotFoundException
     */
    void saveBenchmark(String painting, int score) throws FileNotFoundException;

    /**
     * Method used to save the misclassifications
     * @param predictions list containing all the expected and provided wrong classifications
     * @throws FileNotFoundException
     */
    void saveWrongPredictions(ArrayList<String> predictions) throws FileNotFoundException;

    /**
     * Method used to save the results obtained from europeana and bluemix
     * @param paintings
     * @param creator
     * @param descriptions
     * @param thumbnailsURL
     * @param museums
     * @param keywords
     * @throws FileNotFoundException
     */
    void createSmartDB(ArrayList<String> paintings, ArrayList<String> creator,
                       ArrayList<String> descriptions, ArrayList<String> thumbnailsURL,
                       ArrayList<String> museums, ArrayList<String> keywords) throws FileNotFoundException;

    /**
     * Method used to save the results of a query on Watson Discovery
     * @param queryId unique identifier of the query
     * @param queryResults results of the query
     * @throws FileNotFoundException
     */
    void saveQueryResults(String queryId, List<QueryResult> queryResults) throws FileNotFoundException;
}
