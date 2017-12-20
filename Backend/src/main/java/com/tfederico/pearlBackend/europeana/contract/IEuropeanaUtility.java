package com.tfederico.pearlBackend.europeana.contract;

import com.tfederico.pearlBackend.exceptions.UnknownMuseumException;
import eu.europeana.api.client.exception.EuropeanaApiProblem;

import java.io.IOException;
import java.util.ArrayList;

public interface IEuropeanaUtility {
    void callEuropeana(String museum, String painting,
                  ArrayList<String> descriptions, ArrayList<String> creator,
                  ArrayList<String> thumbnailsURL) throws UnknownMuseumException, IOException, EuropeanaApiProblem;
}
