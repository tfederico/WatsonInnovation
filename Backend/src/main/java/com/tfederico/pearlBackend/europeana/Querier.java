package com.tfederico.pearlBackend.europeana;

import com.tfederico.pearlBackend.europeana.contract.IQuerier;
import eu.europeana.api.client.EuropeanaApi2Client;
import eu.europeana.api.client.exception.EuropeanaApiProblem;
import eu.europeana.api.client.model.EuropeanaApi2Results;
import eu.europeana.api.client.search.query.Api2Query;


import java.io.IOException;

public class Querier implements IQuerier {

    EuropeanaApi2Client europeanaClient;

    public Querier(){
        europeanaClient = new EuropeanaApi2Client();
    }

    public EuropeanaApi2Results search(Api2Query query, int limit, int start) throws IOException, EuropeanaApiProblem {

        return europeanaClient.searchApi2(query, limit, start);
    }
}
