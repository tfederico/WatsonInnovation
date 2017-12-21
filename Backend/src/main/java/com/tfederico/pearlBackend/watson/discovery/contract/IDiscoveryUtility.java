package com.tfederico.pearlBackend.watson.discovery.contract;

import com.ibm.watson.developer_cloud.discovery.v1.model.QueryResult;

import java.util.List;

public interface IDiscoveryUtility {

    /**
     * Method used to perform a natural language query using watson discovery
     * @param query natural language query
     * @return list of results for the query
     */
    List<QueryResult> naturalLanguageQuery(String query);
}
