package com.tfederico.pearlBackend.europeana;

import com.tfederico.pearlBackend.europeana.contract.IQueryResultParser;
import eu.europeana.api.client.model.EuropeanaApi2Results;
import eu.europeana.api.client.model.search.EuropeanaApi2Item;


import java.util.ArrayList;
import java.util.List;

public class QueryResultParser implements IQueryResultParser {

    List<EuropeanaApi2Item> items;

    public QueryResultParser(EuropeanaApi2Results res){
        items = res.getAllItems();
    }

    public ArrayList<List<String>> getTitles() {
        ArrayList<List<String>> data = new ArrayList<>();
        for(EuropeanaApi2Item item : items)
            data.add(item.getTitle());
        return data;
    }

    public ArrayList<String> getEuropeanaURLs() {
        ArrayList<String> data = new ArrayList<String>();
        for(EuropeanaApi2Item item : items)
            if(item.getDcDescription() == null)
                data.add("");
            else
                data.add(item.getObjectURL());
        return data;
    }

    public ArrayList<String> getTypes() {
        ArrayList<String> data = new ArrayList<String>();
        for(EuropeanaApi2Item item : items)
            if(item.getDcDescription() == null)
                data.add("");
            else
                data.add(item.getType());
        return data;
    }

    public ArrayList<String> getCreators() {
        ArrayList<String> data = new ArrayList<String>();
        for(EuropeanaApi2Item item : items)
            if(item.getDcDescription() == null)
                data.add("");
            else
                data.add(item.getDcCreator().get(0));
        return data;
    }

    public ArrayList<String> getThumbnailsURLs() {
        ArrayList<String> data = new ArrayList<String>();
        for(EuropeanaApi2Item item : items)
            if(item.getDcDescription() == null)
                data.add("");
            else
                data.add(item.getEdmPreview().get(0));
        return data;
    }

    public ArrayList<String> getDataProviders() {
        ArrayList<String> data = new ArrayList<String>();
        for(EuropeanaApi2Item item : items)
            if(item.getDcDescription() == null)
                data.add("");
            else
                data.add(item.getDataProvider().get(0));
        return data;
    }

    public ArrayList<String> getDescriptions() {
        ArrayList<String> data = new ArrayList<>();
        for(EuropeanaApi2Item item : items)
            if(item.getDcDescription() == null)
                data.add("");
            else
                data.add(item.getDcDescription().get(0));
        return data;
    }
}
