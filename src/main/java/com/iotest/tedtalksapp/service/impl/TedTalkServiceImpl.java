package com.iotest.tedtalksapp.service.impl;

import com.iotest.tedtalksapp.exception.ResourceNotFoundException;
import com.iotest.tedtalksapp.mapper.TedTalkMapper;
import com.iotest.tedtalksapp.model.TedTalk;
import com.iotest.tedtalksapp.model.TedTalkFilterRequest;
import com.iotest.tedtalksapp.model.TedTalkRequest;
import com.iotest.tedtalksapp.repository.TedTalkCustomRepository;
import com.iotest.tedtalksapp.repository.TedTalkRepository;
import com.iotest.tedtalksapp.service.TedTalkService;
import com.iotest.tedtalksapp.util.GenericUtil;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.*;

@Service
@Data
@RequiredArgsConstructor

public class TedTalkServiceImpl implements TedTalkService {

    private final TedTalkRepository repo;
    private final TedTalkCustomRepository customRepo;
    private final TedTalkMapper tedTalkMapper ;


    public Flux<TedTalk> getAll() {
        return repo.findAll()
                .switchIfEmpty(Flux.empty());
    }

    public Mono<TedTalk> getById(String id) {
        return repo.findById(Long.parseLong(id))
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("TedTalk not found: " + id)));
    }

    public Mono<TedTalk> create(TedTalkRequest talk) {

        if (talk == null) {
            return Mono.error(new IllegalArgumentException("TedTalk cannot be null"));
        }

        return Mono.just(talk)
                .map(tedTalkMapper::toEntity)   // convert request → entity
                .flatMap(repo::save);
    }

    public Mono<Void> delete(String id) {
        return repo.findById(Long.parseLong(id))
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Cannot delete. TedTalk not found: " + id)))
                .then(repo.deleteById(Long.parseLong(id)));
    }

    public Flux<TedTalk> findMostInfluentialForEachYear() {
        return repo.findMostInfluentialByYear()
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("No data is available in DB.")));
    }


    public Mono<TedTalk> mostInfluentialByYear(int year) {
        return repo.findByYearOrderByInfluenceDesc(year)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("No TED Talks available for year: " + year)))
                .next();
    }

    public Flux<TedTalk> searchByCriteria(TedTalkFilterRequest request) {

        return customRepo.findByFilters(request)
                .switchIfEmpty(Flux.error(new RuntimeException("No TED Talks match given filters")));
    }

    public Mono<TedTalk> updateTedTalk(Long id, TedTalkRequest req) {
        if (ObjectUtils.isEmpty(req) || id == null) {
            return Mono.error(new RuntimeException("Bad request."));
        }

        return repo.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("TED Talk not found with id: " + id)))
                .flatMap(existing -> {

                    // Patch update using MapStruct
                    tedTalkMapper.patchUpdate(req,existing);

                    // Recalculate influence score only if views/likes provided
                    long newScore = GenericUtil.calcScore(existing.getViews(), existing.getLikes());
                    existing.setInfluenceScore(newScore);

                    return repo.save(existing);
                });

    }





}

