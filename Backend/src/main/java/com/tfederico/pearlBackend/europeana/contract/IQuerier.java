package com.tfederico.pearlBackend.europeana.contract;

import eu.europeana.api.client.exception.EuropeanaApiProblem;
import eu.europeana.api.client.model.EuropeanaApi2Results;
import eu.europeana.api.client.search.query.Api2Query;


import java.io.IOException;

public interface IQuerier {

    /**
     * Method used to retrieve informations about a painting in the europeana database
     * @param query object representing a query
     * @param limit maximum number of results
     * @param start
     * @return a collection of results for the query
     * @throws IOException
     * @throws EuropeanaApiProblem
     */
    EuropeanaApi2Results search(Api2Query query, int limit, int start) throws IOException, EuropeanaApiProblem;
}
