package com.tfederico.pearlBackend.europeana.contract;


import eu.europeana.api.client.search.query.Api2Query;

public interface IQueryBuilder {

    void createNewQuery();

    Api2Query getQuery();

    void setCollectionName(String collectionName);

    void setCountry(String creator);

    void setCreator(String creator);

    void setDataProvider(String dataProvider);

    void setTitle(String title);

    void setType(String type);
}
