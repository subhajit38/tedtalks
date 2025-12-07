package com.iotest.tedtalkapp.service;

import com.iotest.tedtalksapp.model.TedTalk;
import com.iotest.tedtalksapp.repository.TedTalkRepository;
import com.iotest.tedtalksapp.service.impl.CSVImportServiceImpl;
import com.iotest.tedtalksapp.util.CsvUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CSVImportServiceTest {

    private TedTalkRepository repo;
    private CSVImportServiceImpl service;

    @BeforeEach
    void setup() {
        repo = mock(TedTalkRepository.class);
        service = new CSVImportServiceImpl(repo);
    }

    @Test
    void testImportCsv() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        LocalDate date = YearMonth.parse("June 2020", formatter).atDay(1);

        TedTalk row1 = new TedTalk(Long.parseLong("1"),"Talk A","Author A",date,1000,200,"http://a.com",1234);
        TedTalk row2 = new TedTalk(Long.parseLong("2"),"Talk B","Author B",date,1002,2001,"http:/b.com",1211);

        try (MockedStatic<CsvUtils> csvUtilsMock = mockStatic(CsvUtils.class)) {

            csvUtilsMock
                    .when(() -> CsvUtils.readCsv("test.csv"))
                    .thenReturn(Flux.just(row1, row2));

            when(repo.save(row1)).thenReturn(Mono.just(row1));
            when(repo.save(row2)).thenReturn(Mono.just(row2));

            Mono<Long> result = service.importCsv();

            StepVerifier.create(result)
                    .expectNext(2L)
                    .verifyComplete();

            verify(repo, times(2)).save(any(TedTalk.class));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
