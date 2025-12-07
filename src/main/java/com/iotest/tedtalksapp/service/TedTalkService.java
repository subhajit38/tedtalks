package com.iotest.tedtalksapp.service;

import com.iotest.tedtalksapp.model.TedTalk;
import com.iotest.tedtalksapp.model.TedTalkFilterRequest;
import com.iotest.tedtalksapp.model.TedTalkRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TedTalkService {
    Flux<TedTalk> getAll();
    Mono<TedTalk> getById(String id);
    Mono<TedTalk> create(TedTalkRequest talk);
    Mono<Void> delete(String id);
    Flux<TedTalk> findMostInfluentialForEachYear();
    Mono<TedTalk> mostInfluentialByYear(int year);
    Flux<TedTalk> searchByCriteria(TedTalkFilterRequest request);
    Mono<TedTalk> updateTedTalk(Long id, TedTalkRequest req);



}
