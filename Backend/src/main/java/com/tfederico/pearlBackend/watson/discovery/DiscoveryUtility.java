package com.tfederico.pearlBackend.watson.discovery;

import com.ibm.watson.developer_cloud.discovery.v1.model.QueryResult;
import com.tfederico.libris.text.discovery.contract.IIBMDiscoveryUtility;
import com.tfederico.libris.text.discovery.ibm.IBMDiscoveryUtility;
import com.tfederico.pearlBackend.watson.discovery.contract.IDiscoveryUtility;

import java.util.List;

public class DiscoveryUtility implements IDiscoveryUtility{

    private IIBMDiscoveryUtility discoveryUtility;
    private static String environmentId;
    private static String collectionId;

    public DiscoveryUtility(){
        discoveryUtility = new IBMDiscoveryUtility();
    }

    public static void setEnvironmentId(String envId){
        environmentId = envId;
    }

    public static void setCollectionId(String collId){
        collectionId = collId;
    }

    @Override
    public List<QueryResult> naturalLanguageQuery(String query) {
        return discoveryUtility.queryCollection(environmentId,collectionId,query).getResults();
    }
}
