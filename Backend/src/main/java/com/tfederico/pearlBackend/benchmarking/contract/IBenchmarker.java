package com.tfederico.pearlBackend.benchmarking.contract;

import java.io.IOException;

public interface IBenchmarker {
    void benchmark(String path) throws IOException;
}
