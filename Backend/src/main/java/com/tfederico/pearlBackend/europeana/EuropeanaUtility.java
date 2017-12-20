package com.tfederico.pearlBackend.europeana;

import com.tfederico.pearlBackend.europeana.contract.IEuropeanaUtility;
import com.tfederico.pearlBackend.europeana.contract.IQuerier;
import com.tfederico.pearlBackend.europeana.contract.IQueryBuilder;
import com.tfederico.pearlBackend.exceptions.UnknownMuseumException;
import eu.europeana.api.client.exception.EuropeanaApiProblem;
import eu.europeana.api.client.model.EuropeanaApi2Results;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class EuropeanaUtility implements IEuropeanaUtility{

    private IQueryBuilder queryBuilder;

    public EuropeanaUtility(){
        queryBuilder = new QueryBuilder();
    }
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
    @Override
    public void callEuropeana(String museum, String painting, ArrayList<String> descriptions, ArrayList<String> creator, ArrayList<String> thumbnailsURL) throws UnknownMuseumException, IOException, EuropeanaApiProblem {
        queryBuilder.createNewQuery();
        queryBuilder.setTitle(painting);

        if(Objects.equals(museum, "Mauritshuis"))
            queryBuilder.setCollectionName("2021672_Ag_NL_DigitaleCollectie_Mauritshuis");
        else if (Objects.equals(museum, "Rijksmuseum"))
            queryBuilder.setCollectionName("90402_M_NL_Rijksmuseum");
        else
            throw new UnknownMuseumException();

        QueryResultParser resultParser;

        EuropeanaApi2Results results;
        IQuerier querier = new Querier();
        results = querier.search(queryBuilder.getQuery(),1,0);
        resultParser = new QueryResultParser(results);

        descriptions.add(resultParser.getDescriptions().get(0));
        creator.add(resultParser.getCreators().get(0));
        thumbnailsURL.add(resultParser.getThumbnailsURLs().get(0));
    }
}
