package com.iotest.tedtalksapp.service.impl;

import com.iotest.tedtalksapp.model.TedTalk;
import com.iotest.tedtalksapp.repository.TedTalkRepository;
import com.iotest.tedtalksapp.service.CSVService;
import com.iotest.tedtalksapp.util.CsvUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class CSVImportServiceImpl implements CSVService {

    private final TedTalkRepository repo;
    private static final String FILENAME = "test.csv";

    public Mono<Long> importCsv() throws IOException {
        return CsvUtils.readCsv(FILENAME)
                .flatMap(repo::save)
                .count();
    }
}
