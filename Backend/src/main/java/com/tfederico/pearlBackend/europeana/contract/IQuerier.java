package com.tfederico.pearlBackend.europeana.contract;


import eu.europeana.api.client.model.EuropeanaApi2Results;
import eu.europeana.api.client.search.query.Api2Query;

import java.io.IOException;

public interface IQuerier {

    EuropeanaApi2Results search(Api2Query query, int limit, int start) throws IOException;
}
