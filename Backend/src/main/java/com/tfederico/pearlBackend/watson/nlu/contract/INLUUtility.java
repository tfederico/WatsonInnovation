package com.tfederico.pearlBackend.watson.nlu.contract;

import java.util.ArrayList;

public interface INLUUtility {
    /**
     * Method used to extract keywords from a piece of text using watson natural language discovery
     * @param texts list of textx
     * @return list of keywords
     */
    public ArrayList<String> getKeywordsFromTexts(ArrayList<String> texts);
}
