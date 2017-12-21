package com.tfederico.pearlBackend.benchmarking.contract;

import java.io.IOException;

public interface IBenchmarker {

    /**
     * Method used to evaluate the goodness of the classifiers
     * @param path path to the folder containing the images that have to be classified
     * @throws IOException
     */
    void benchmark(String path) throws IOException;
}
