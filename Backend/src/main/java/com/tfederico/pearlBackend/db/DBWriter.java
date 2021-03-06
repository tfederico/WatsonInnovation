package com.tfederico.pearlBackend.db;

import com.ibm.watson.developer_cloud.discovery.v1.model.QueryResult;
import com.tfederico.pearlBackend.db.contract.IDBWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DBWriter implements IDBWriter {

    /**
     *
     * @param paintings names of the paintings
     * @param matrix matrix indicating the number of common keywords between each couple of paintings
     * @param keys list of set of keywords for each painting
     * @throws FileNotFoundException
     */
    @Override
    public void saveItemSimilarities(ArrayList<String> paintings, Integer[][] matrix, List<Set<String>> keys) throws FileNotFoundException {
        String divider = "\t";

        PrintWriter pw = new PrintWriter(new File("res/similarItems.csv"));
        StringBuilder sb = new StringBuilder();

        sb.append(" ").append(divider);
        for(String paint : paintings)
            sb.append(paint).append(divider);
        sb.append("\n");
        for (int j = 0; j < keys.size(); j++) {
            sb.append(paintings.get(j)).append(divider);
            for (int k = 0; k < keys.size(); k++)
                sb.append(matrix[j][k]).append(divider);

            sb.append("\n");
        }

        pw.write(sb.toString());

        pw.close();
    }

    /**
     * Method used to save the benchmark result
     * @param painting name of the benchmarked painting
     * @param score number of correct predicitions
     * @throws FileNotFoundException
     */
    @Override
    public void saveBenchmark(String painting, int score) throws FileNotFoundException {

        //todo add check if the file exists, if no create and add header
        String divider = "\t";
        PrintWriter pos = new PrintWriter(new FileOutputStream(new File("res/benchmark.csv"),true));
        StringBuilder sPos = new StringBuilder();

        sPos.append(painting).append(divider).append(score).append("\n");

        pos.append(sPos.toString());

        pos.close();
    }

    /**
     * Method used to save the misclassifications
     * @param predictions list containing all the expected and provided wrong classifications
     * @throws FileNotFoundException
     */
    @Override
    public void saveWrongPredictions(ArrayList<String> predictions) throws FileNotFoundException {

        //todo add check if the file exists, if no create and add header
        String divider = "\t";

        PrintWriter pw = new PrintWriter(new FileOutputStream(new File("res/wrongPredictions.csv"),true));

        StringBuilder sb = new StringBuilder();

        for(String s : predictions){
            sb.append(s.split("_")[0]);
            sb.append(divider);
            sb.append(s.split("_")[1]);
            sb.append("\n");

        }

        pw.append(sb.toString());
        pw.close();
    }

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
    @Override
    public void createSmartDB(ArrayList<String> paintings, ArrayList<String> creator,
                                        ArrayList<String> descriptions, ArrayList<String> thumbnailsURL,
                                        ArrayList<String> museums, ArrayList<String> keywords) throws FileNotFoundException {

        String divider = "\t";
        String header = "id"+divider+"painting"+divider+"author"+divider+"description"+divider+
                "thumbnailURL"+divider+"museum"+divider+"keywords\n";
        PrintWriter pw = new PrintWriter(new File("res/smartdb.csv"));
        StringBuilder sb = new StringBuilder();
        sb.append(header);


        for(int i = 0; i<paintings.size(); i++){
            sb.append(paintings.get(i).replace(" ","_").replace(",",""));
            sb.append(divider);
            sb.append(paintings.get(i));
            sb.append(divider);
            sb.append(creator.get(i));
            sb.append(divider);
            sb.append(descriptions.get(i).replace("\n",""));
            sb.append(divider);
            sb.append(thumbnailsURL.get(i));
            sb.append(divider);
            sb.append(museums.get(i));
            sb.append(divider);
            sb.append(keywords.get(i));
            sb.append("\n");
        }

        pw.write(sb.toString());
        pw.close();
    }

    /**
     * Method used to save the results of a query on Watson Discovery
     * @param queryId unique identifier of the query
     * @param queryResults results of the query
     * @throws FileNotFoundException
     */
    @Override
    public void saveQueryResults(String queryId, List<QueryResult> queryResults) throws FileNotFoundException {
        String divider = "\t";
        String header = "score"+divider+"painting"+divider+"author"+divider+"description"+divider+
                "thumbnailURL"+divider+"museum\n";
        PrintWriter pw = new PrintWriter(new File("res/queriesResults/query_"+queryId+".csv"));
        StringBuilder sb = new StringBuilder();
        sb.append(header);

        for(QueryResult q : queryResults){
            sb.append(q.getResultMetadata().getScore());
            sb.append(divider);
            sb.append(q.get("painting"));
            sb.append(divider);
            sb.append(q.get("author"));
            sb.append(divider);
            sb.append(q.get("description"));
            sb.append(divider);
            sb.append(q.get("thumbnailURL"));
            sb.append(divider);
            sb.append(q.get("museum"));
            sb.append("\n");
        }

        pw.write(sb.toString());
        pw.close();
    }
}
