package com.krydtin.user.utils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    public static LocalDate currentLocalDateWithUTC() {
        return LocalDate.now(ZoneId.of("UTC+7"));
    }

    public static String currentDate() {
        return YYYY_MM_DD.format(currentLocalDateWithUTC());
    }

    public static final DateTimeFormatter YYYY_MM_DD = DateTimeFormatter
            .ofPattern("yyyyMMdd")
            .withZone(ZoneId.of("UTC+7"));

}
