package com.iotest.tedtalksapp.repository;

import com.iotest.tedtalksapp.model.TedTalk;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.*;

import java.util.concurrent.ConcurrentHashMap;

@Repository
//@RequiredArgsConstructor
public interface TedTalkRepository extends ReactiveCrudRepository<TedTalk, Long> {


//    private final ConcurrentHashMap<String, TedTalk> store = new ConcurrentHashMap<>();
//
//    public Mono<TedTalk> save(TedTalk talk) {
//        store.put(talk.uniqueId(), talk);
//        return Mono.just(talk);
//    }
//
//    public Flux<TedTalk> findAll() {
//        return Flux.fromIterable(store.values());
//    }
//
//    public Mono<TedTalk> findById(String id) {
//        return Mono.justOrEmpty(store.get(id));
//    }
//
//    public Mono<Void> deleteById(String id) {
//        store.remove(id);
//        return Mono.empty();
//    }
//
//    public boolean exists(String id) {
//        return store.containsKey(id);
//    }

    // Fetch talks by year, sorted by views*2 + likes descending
    @Query("SELECT * FROM ted_talks " +
            "WHERE YEAR(date) = :year " +
            "ORDER BY INFLUENCE_SCORE DESC")
    Flux<TedTalk> findByYearOrderByInfluenceDesc(int year);

    @Query("""
    SELECT t.*
    FROM ted_talks t
    WHERE t.INFLUENCE_SCORE = (
        SELECT MAX(t2.INFLUENCE_SCORE)
        FROM ted_talks t2
        WHERE EXTRACT(YEAR FROM t2.date) = EXTRACT(YEAR FROM t.date)
    )
    ORDER BY EXTRACT(YEAR FROM t.date)
""")
    Flux<TedTalk> findMostInfluentialByYear();
}


