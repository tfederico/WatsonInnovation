package com.tfederico.pearlBackend.europeana.contract;

import com.tfederico.pearlBackend.exceptions.UnknownMuseumException;
import eu.europeana.api.client.exception.EuropeanaApiProblem;

import java.io.IOException;
import java.util.ArrayList;

public interface IEuropeanaUtility {
    /**
     * Method used to retrieve informations about the different paintings stored in europeana
     * @param museum List of museums where the paintings are stored
     * @param painting List of paintings queried on europeana
     * @param descriptions List of descriptions of the paintings, retrieved from europeana
     * @param creator List of painters of the paintings, retrieved from europeana
     * @param thumbnailsURL List of URL to the thumbnail of the paintings, retrieved from europeana
     * @throws UnknownMuseumException
     * @throws IOException
     * @throws EuropeanaApiProblem
     */
    void callEuropeana(String museum, String painting,
                  ArrayList<String> descriptions, ArrayList<String> creator,
                  ArrayList<String> thumbnailsURL) throws UnknownMuseumException, IOException, EuropeanaApiProblem;
}
