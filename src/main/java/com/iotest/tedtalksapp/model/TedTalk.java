package com.iotest.tedtalksapp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import lombok.*;
import java.time.LocalDate;

//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Table(name = "ted_talks")
public class TedTalk {
    @Id
    private Long id;
    private String title;
    private String author;
    private LocalDate date;
    private long views;
    private long likes;
    private String link;
    private long influenceScore;

    // Unique identifier = title + author
    public String uniqueId() {
        return (title + "_" + author).replace(" ", "_").toLowerCase();
    }
}

