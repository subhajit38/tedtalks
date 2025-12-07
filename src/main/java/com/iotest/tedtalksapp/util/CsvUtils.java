package com.iotest.tedtalksapp.util;

import com.iotest.tedtalksapp.model.TedTalk;
import com.opencsv.CSVReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Slf4j
public class CsvUtils {

    public static Flux<TedTalk> readCsv(String filePath) {
        return Flux.using(
                () -> new CSVReader(new InputStreamReader(
                        new ClassPathResource(filePath).getInputStream()
                )),
                reader -> Flux.fromIterable(reader)
                        .skip(1) // skip header
                        .flatMap(CsvUtils::safeParseRow),
                reader -> {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        // ignore
                    }
                }
        );
    }


    public static Flux<TedTalk> readCsv1(String filePath) throws IOException {
        try {
            File file = new ClassPathResource(filePath).getFile();
            return Flux.using(
                    () -> createReader(filePath),
                    reader -> Flux.fromIterable(reader)
                            .skip(1)  // header
                            .flatMap(CsvUtils::safeParseRow),
                    CsvUtils::safeClose
            );
        }catch (IOException e) {
            return Flux.error(new RuntimeException("CSV file not found in classpath: " + filePath));
        }
    }

    // Create reader with exception handling
    private static CSVReader createReader(String path) {
        try {
            return new CSVReader(new FileReader(path));
        } catch (FileNotFoundException e) {
            log.error("CSV file not found: {}", path);
            throw new RuntimeException("CSV file not found at " + path, e);
        }
    }

    // Ensure reader closes safely
    private static void safeClose(CSVReader reader) {
        try {
            reader.close();
        } catch (Exception e) {
            log.warn("Failed to close CSV reader", e);
        }
    }

    // Parse row and return Flux<TedTalk>
    private static Mono<TedTalk> safeParseRow(String[] row) {
        return Mono.fromCallable(() -> {
            try {
                return parseRow(row);
            } catch (Exception e) {
                log.error("Error parsing CSV row:: {} and message :: {}", String.join(" | ", row), e.getMessage());
                return null; // will be filtered out
            }
        }).filter(t -> t != null); // skip invalid rows
    }

    // Actual parsing logic
    private static TedTalk parseRow(String[] columns) {

        if (columns.length < 6)
            throw new IllegalArgumentException("Invalid CSV row length: " + columns.length);

        String title = columns[0].trim();
        String author = columns[1].trim();
        String dateStr = columns[2].trim();
        long views = GenericUtil.safeLong(columns[3].trim());
        long likes = GenericUtil.safeLong(columns[4].trim());
        String link = columns[5].trim();

        //if(!GenericUtil.isPastOrToday(dateStr)) {
          //  return null;
        //}
        LocalDate date  = GenericUtil.parseValidDate(dateStr);


        return TedTalk.builder()
                .title(title)
                .author(author)
                .date(date)
                .views(views)
                .likes(likes)
                .link(link)
                .influenceScore(GenericUtil.calcScore(views,likes))
                .build();
    }

    // Date parsing with fallback
    private static LocalDate parseDate(String dateStr) {
        try {

            return YearMonth.parse(dateStr, DateTimeFormatter.ofPattern("MMMM yyyy"))
                    .atDay(1);

        } catch (DateTimeParseException e1) {
            log.warn("Primary date format failed for '{}', trying fallback...", dateStr);
            try {
                return YearMonth.parse(dateStr, DateTimeFormatter.ofPattern("MMM yyyy"))
                        .atDay(1);
            } catch (DateTimeParseException e2) {
                throw new RuntimeException("Unable to parse date: " + dateStr, e2);
            }
        }
    }
}






