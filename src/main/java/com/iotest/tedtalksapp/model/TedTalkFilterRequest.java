package com.iotest.tedtalksapp.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class TedTalkFilterRequest {
    private String title;
    private String author;
    private LocalDate date;
}
