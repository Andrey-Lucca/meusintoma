package br.com.meusintoma.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class SystemClockUtils {
    public static LocalDate getCurrentDate() {
        return LocalDate.now();
    }

    public static LocalTime getCurrentTime() {
        return LocalTime.now();
    }

    public static LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }
}
