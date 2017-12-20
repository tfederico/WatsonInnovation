package com.tfederico.pearlBackend.watson.visualRecognition.contract;

import java.io.IOException;
import java.util.ArrayList;

public interface IModelTrainer {
    public void trainModels(ArrayList<String> paintings) throws IOException;
}
