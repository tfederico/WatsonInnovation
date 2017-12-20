package com.tfederico.pearlBackend.watson.nlu.contract;

import java.util.ArrayList;

public interface INLUUtility {
    public ArrayList<String> getKeywordsFromTexts(ArrayList<String> texts);
}
