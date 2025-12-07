package com.iotest.tedtalksapp.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Data
@RequiredArgsConstructor
public class TedTalkRequest {
    private String title;
    private String author;
    private LocalDate date;
    private long views;
    private long likes;
    private String link;
}
