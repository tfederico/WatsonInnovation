package com.tfederico.pearlBackend.watson.discovery.contract;

import com.ibm.watson.developer_cloud.discovery.v1.model.QueryResult;

import java.util.List;

public interface IDiscoveryUtility {

    List<QueryResult> naturalLanguageQuery(String query);
}
