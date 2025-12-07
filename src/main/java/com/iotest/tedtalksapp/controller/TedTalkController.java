package com.iotest.tedtalksapp.controller;

import com.iotest.tedtalksapp.model.TedTalk;
import com.iotest.tedtalksapp.model.TedTalkFilterRequest;
import com.iotest.tedtalksapp.model.TedTalkRequest;
import com.iotest.tedtalksapp.service.impl.CSVImportServiceImpl;
import com.iotest.tedtalksapp.service.impl.TedTalkServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.*;

import java.io.IOException;

@RestController
@RequestMapping("/tedtalksApi")
@RequiredArgsConstructor
@Slf4j
public class TedTalkController {

    private final TedTalkServiceImpl service;
    private final CSVImportServiceImpl csvImportService;

    @GetMapping("/import-csv")
    @Operation(summary = "Load csv file in DB")
    public Flux<TedTalk> importCsv() throws IOException {
        return csvImportService.importCsv();
    }

    @GetMapping
    public Flux<TedTalk> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Mono<TedTalk> getById(@PathVariable String id) {
        return service.getById(id);
    }

    @PostMapping
    public Mono<TedTalk> create(@RequestBody TedTalkRequest talk) {
        return service.create(talk);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable String id) {
        return service.delete(id);
    }

    @GetMapping("/influentialEachYear")
    public Flux<TedTalk> mostInfluential() {
        return service.findMostInfluentialForEachYear();
    }

    @PostMapping("/search")
    public Flux<TedTalk> search(@RequestBody TedTalkFilterRequest tedTalkFilterRequest) {
        return service.searchByCriteria(tedTalkFilterRequest);
    }

    @GetMapping("/influential/{year}")
    public Mono<TedTalk> getMostInfluentialByYear(@PathVariable int year) {
        return service.mostInfluentialByYear(year);
    }

    @PatchMapping("/patch-tedTalk/{id}")
    public Mono<TedTalk> updateTalk(
                @PathVariable  @NonNull Long id,
            @RequestBody @Validated TedTalkRequest updateRequest) {

        return service.updateTedTalk(id, updateRequest);
    }
}
