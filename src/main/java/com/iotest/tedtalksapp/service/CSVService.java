package com.iotest.tedtalksapp.service;

import reactor.core.publisher.Mono;

public interface CSVService {
    Mono<Long> importCsv() throws Exception;
}
