package com.tfederico.pearlBackend.europeana;

import com.tfederico.pearlBackend.europeana.contract.IQuerier;
import eu.europeana.api.client.EuropeanaApi2Client;
import eu.europeana.api.client.exception.EuropeanaApiProblem;
import eu.europeana.api.client.model.EuropeanaApi2Results;
import eu.europeana.api.client.search.query.Api2Query;


import java.io.IOException;

public class Querier implements IQuerier {

    EuropeanaApi2Client europeanaClient;

    Querier(){
        europeanaClient = new EuropeanaApi2Client();
    }

    /**
     * Method used to retrieve informations about a painting in the europeana database
     * @param query object representing a query
     * @param limit maximum number of results
     * @param start
     * @return a collection of results for the query
     * @throws IOException
     * @throws EuropeanaApiProblem
     */
    public EuropeanaApi2Results search(Api2Query query, int limit, int start) throws IOException, EuropeanaApiProblem {

        return europeanaClient.searchApi2(query, limit, start);
    }
}
