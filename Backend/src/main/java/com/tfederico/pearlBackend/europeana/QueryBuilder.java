package com.tfederico.pearlBackend.europeana;

import com.tfederico.pearlBackend.europeana.contract.IQueryBuilder;
import eu.europeana.api.client.search.query.Api2Query;

public class QueryBuilder implements IQueryBuilder{

    private Api2Query query;

    public void createNewQuery() {
        query = new Api2Query();
    }

    public Api2Query getQuery() {
        return query;
    }

    public void setCollectionName(String collectionName) {
        query.setCreator(collectionName);
    }

    public void setCountry(String creator) {
        query.setCreator(creator);
    }

    public void setCreator(String creator) {
        query.setCreator(creator);
    }

    public void setDataProvider(String dataProvider) {
        query.setDataProvider(dataProvider);
    }

    public void setTitle(String title) {
        query.setTitle(title);
    }

    public void setType(String type) {
        query.setType(type);
    }
}
