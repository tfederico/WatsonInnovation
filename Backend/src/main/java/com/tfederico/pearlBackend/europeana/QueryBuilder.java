package com.tfederico.pearlBackend.europeana;

import com.tfederico.pearlBackend.europeana.contract.IQueryBuilder;
import eu.europeana.api.client.search.query.Api2Query;

/**
 * class used to build a query
 */
public class QueryBuilder implements IQueryBuilder{

    private Api2Query query;

    public void createNewQuery() {
        query = new Api2Query();
    }

    @Override
    public Api2Query getQuery() {
        return query;
    }

    @Override
    public void setCollectionName(String collectionName) {
        query.setCollectionName(collectionName);
    }

    @Override
    public void setCountry(String creator) {
        query.setCountry(creator);
    }

    @Override
    public void setCreator(String creator) {
        query.setCreator(creator);
    }

    @Override
    public void setDataProvider(String dataProvider) {
        query.setDataProvider(dataProvider);
    }

    @Override
    public void setTitle(String title) {
        query.setTitle(title);
    }

    @Override
    public void setType(String type) {
        query.setType(type);
    }

    @Override
    public void setLanguage(String language) {
        query.setLanguage(language);
    }
}
