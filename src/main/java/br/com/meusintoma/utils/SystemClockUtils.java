package br.com.meusintoma.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class SystemClockUtils {
    public static LocalDate getCurrentDate() {
        return LocalDate.now();
        //return LocalDate.of(2025, 5, 1);
    }

    public static LocalTime getCurrentTime() {
        return LocalTime.now();
        //return LocalTime.of(8, 0, 0);
    }

    public static LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }
}
