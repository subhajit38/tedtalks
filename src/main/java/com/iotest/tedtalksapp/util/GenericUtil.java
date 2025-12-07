package com.iotest.tedtalksapp.util;

import com.iotest.tedtalksapp.model.TedTalk;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@UtilityClass
@Slf4j
public class GenericUtil {

    public static long calcScore(long views, long likes ) {
        if(views!= 0) {
            return views + likes * 3 + likes / views * 1000;
        }
        return 0 ;
    }

    public static boolean isPastOrToday(String dateStr) {
        LocalDate givenDate = YearMonth.parse(
                dateStr,
                DateTimeFormatter.ofPattern("MMMM yyyy")
        ).atDay(1);

        LocalDate today = LocalDate.now();

        // return false if given date is in the future
        return !givenDate.isAfter(today);
    }

    public static long safeLong(String s) {
        try {
            return Long.parseLong(s.trim());
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid views/likes data only accept long but provided ::"+s);
        }
    }

    public static LocalDate parseValidDate(String dateStr) {
        if (dateStr == null || dateStr.isBlank()) {
            throw new IllegalArgumentException("Date cannot be null or empty");
        }

        dateStr = dateStr.trim();

        // Expected pattern: "MMMM yyyy" (e.g., December 2021)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.ENGLISH);

        // Check structure: must contain valid month + year
        String[] parts = dateStr.split(" ");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid date format. Expected 'MMMM yyyy'. Found: " + dateStr);
        }

        String monthPart = parts[0];
        String yearPart = parts[1];

        // Validate year numeric
        int year;
        try {
            year = Integer.parseInt(yearPart);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid year: " + yearPart);
        }

        // Validate year > 1000 (business rules)
        if (year < 1900 || year > 9999) {
            throw new IllegalArgumentException("Invalid year: " + year + ". Year must be between 1900 and 9999.");
        }

        // Now safely parse
        try {
            return YearMonth.parse(dateStr, formatter).atDay(1);
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable to parse date: " + dateStr);
        }
    }



}

