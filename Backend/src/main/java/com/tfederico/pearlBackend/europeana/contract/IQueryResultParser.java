package com.tfederico.pearlBackend.europeana.contract;

import java.util.ArrayList;
import java.util.List;

public interface IQueryResultParser {

    ArrayList<List<String>> getTitles();

    ArrayList<String> getEuropeanaURLs();

    ArrayList<String> getTypes();

    ArrayList<String> getCreators();

    ArrayList<String> getThumbnailsURLs();

    ArrayList<String> getDataProviders();

    ArrayList<String> getDescriptions();
}
