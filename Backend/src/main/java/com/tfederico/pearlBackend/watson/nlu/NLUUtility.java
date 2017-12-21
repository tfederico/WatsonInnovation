package com.tfederico.pearlBackend.watson.nlu;

import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.KeywordsResult;
import com.tfederico.libris.text.naturalLanguageUnderstanding.contract.IIBMNaturalLanguageUnderstandingUtility;
import com.tfederico.libris.text.naturalLanguageUnderstanding.ibm.IBMNaturalLanguageUnderstandingUtility;
import com.tfederico.pearlBackend.watson.nlu.contract.INLUUtility;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NLUUtility implements INLUUtility{
    IIBMNaturalLanguageUnderstandingUtility nlu;

    public NLUUtility(){
        nlu = new IBMNaturalLanguageUnderstandingUtility();
    }

    /**
     * Method used to extract keywords from a piece of text using watson natural language discovery
     * @param texts list of textx
     * @return list of keywords
     */
    @Override
    public ArrayList<String> getKeywordsFromTexts(ArrayList<String> texts) {
        ArrayList<String> keyword = new ArrayList<>(texts.size());


        for (String description : texts) {
            List<KeywordsResult> keywordsResultList = nlu.getKeywordsFromText(description);
            StringBuilder builder = new StringBuilder();
            Set<String> set = new HashSet<>();

            for (KeywordsResult keywordsResult : keywordsResultList) //remove duplicates
                set.add(keywordsResult.getText());

            for (String s : set)
                builder.append(s.toLowerCase()).append(" ");

            int length = builder.toString().length();
            keyword.add(builder.toString().substring(0, length - 1));
        }
        return keyword;
    }
}
