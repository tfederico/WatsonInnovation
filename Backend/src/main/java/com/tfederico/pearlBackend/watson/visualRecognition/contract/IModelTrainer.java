package com.tfederico.pearlBackend.watson.visualRecognition.contract;

import java.io.IOException;
import java.util.ArrayList;

public interface IModelTrainer {

    /**
     * Method used to train multiple model on watson visual recognition, using
     * the paintings name to retrieve the files
     * @param paintings list of paintings names
     * @throws IOException
     */
    void trainModels(ArrayList<String> paintings) throws IOException;
}
