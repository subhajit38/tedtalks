package com.iotest.tedtalksapp.util;


import com.iotest.tedtalksapp.model.TedTalk;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.net.URL;
import java.nio.file.Paths;

public class CsvUtilsTest {

    @Test
    void testReadCsv() throws Exception {

        // Load CSV from test resources
        URL resource = getClass().getClassLoader().getResource("test.csv");
        String path = Paths.get(resource.toURI()).toString();

        Flux<TedTalk> flux = CsvUtils.readCsv(path);

        StepVerifier.create(flux);
    }
}

