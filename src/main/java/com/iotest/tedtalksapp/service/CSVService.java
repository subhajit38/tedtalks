package com.iotest.tedtalksapp.service;

import com.iotest.tedtalksapp.model.TedTalk;
import reactor.core.publisher.Flux;

public interface CSVService {
    Flux<TedTalk> importCsv() throws Exception;
}
