package com.kcd.KCDSpringBatch.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public final class DateUtil {
    static final String pattern = "yyyy-MM-dd HH:mm:ss.SSS";

    static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

    static final SimpleDateFormat format = new SimpleDateFormat(pattern);

    /*
    // base version
    public static LocalDateTime getLocalDateTime(String str) {
        return LocalDateTime.parse(str, formatter);
    }
     */

    public static LocalDateTime getLocalDateTime(String str) {
        if(str.length() == 26) return getLocalDateTime(str, "yyyy-MM-dd HH:mm:ss.SSSSSS");
        else if(str.length() == 19) return getLocalDateTime(str, "yyyy-MM-dd HH:mm:ss");
        else return LocalDateTime.parse(str, formatter);
    }

    public static LocalDateTime getLocalDateTime(String str, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime.parse(str, formatter);
    }

    public static String getPattern() {
        return pattern;
    }

    public static LocalDateTime getLocalDateTime(Date date) {
        return new Timestamp(date.getTime()).toLocalDateTime();
    }

    public static String getStringTime(Date date) {
        return format.format(date);
    }

    public static LocalDateTime getCurrentLocalDateTime() {
        return LocalDateTime.now();
    }

    public static Date getCurrentDate() {
        return Timestamp.valueOf(LocalDateTime.now().format(formatter));
    }

    public static DateFormat getDateFormat() { return format; }
}
