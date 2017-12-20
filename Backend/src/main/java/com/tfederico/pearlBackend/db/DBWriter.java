package com.tfederico.pearlBackend.db;

import com.ibm.watson.developer_cloud.discovery.v1.model.QueryResult;
import com.tfederico.pearlBackend.db.contract.IDBWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DBWriter implements IDBWriter {
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

    @Override
    public void saveBenchmark(ArrayList<String> paintings, ArrayList<Integer> score) throws FileNotFoundException {
        String divider = "\t";
        PrintWriter pos = new PrintWriter(new File("res/benchmark.csv"));
        StringBuilder sPos = new StringBuilder();
        sPos.append("Painting").append(divider).append("Correct predictions over a set of 5\n");

        for(int i = 0; i < paintings.size(); i++){
            sPos.append(paintings.get(i));
            sPos.append(divider);
            sPos.append(score.get(i));
            sPos.append("\n");
        }

        pos.write(sPos.toString());

        pos.close();
    }

    @Override
    public void saveWrongPredictions(ArrayList<String> predictions) throws FileNotFoundException {
        String divider = "\t";

        PrintWriter pw = new PrintWriter(new File("res/wrongPredictions.csv"));

        StringBuilder sb = new StringBuilder();

        sb.append("Real").append(divider).append("Predicted\n");

        for(String s : predictions){
            sb.append(s.split("_")[0]);
            sb.append(divider);
            sb.append(s.split("_")[1]);
            sb.append("\n");

        }

        pw.write(sb.toString());
        pw.close();
    }

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
