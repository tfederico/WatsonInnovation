package com.tfederico.pearlBackend;

import com.tfederico.pearlBackend.db.PaintingDbReader;
import com.tfederico.pearlBackend.db.contract.IPaintingDbReader;
import com.tfederico.pearlBackend.europeana.Querier;
import com.tfederico.pearlBackend.europeana.QueryBuilder;
import com.tfederico.pearlBackend.europeana.QueryResultParser;
import com.tfederico.pearlBackend.europeana.contract.IQuerier;
import com.tfederico.pearlBackend.europeana.contract.IQueryBuilder;
import com.tfederico.pearlBackend.exceptions.UnknownMuseumException;
import eu.europeana.api.client.exception.EuropeanaApiProblem;
import eu.europeana.api.client.model.EuropeanaApi2Results;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class Main {

    public static void main(String[] args) throws IOException, UnknownMuseumException, EuropeanaApiProblem {

        System.out.println("Working Directory = " +
                System.getProperty("user.dir"));
        IPaintingDbReader paintingDbReader = new PaintingDbReader();

        paintingDbReader.openDatabase();

        paintingDbReader.readDatabase();

        ArrayList<String> paintings = paintingDbReader.getPaintings();

        ArrayList<String> museums = paintingDbReader.getMuseums();

        IQuerier querier = new Querier();

        IQueryBuilder queryBuilder = new QueryBuilder();

        EuropeanaApi2Results results;

        QueryResultParser resultParser;

        for(int i = 0; i < paintings.size(); i++){
            queryBuilder.createNewQuery();
            queryBuilder.setTitle(paintings.get(i));

            if(Objects.equals(museums.get(i), "Mauritshuis"))
                queryBuilder.setCollectionName("2021672_Ag_NL_DigitaleCollectie_Mauritshuis");
            else if (Objects.equals(museums.get(i), "Rijksmuseum"))
                queryBuilder.setCollectionName("90402_M_NL_Rijksmuseum");
            else
                throw new UnknownMuseumException();

            results = querier.search(queryBuilder.getQuery(),1,0);
            resultParser = new QueryResultParser(results);

            System.out.println(resultParser.getTitles());
        }
    }
}

