package ru.javaops.bootjava.util;

import java.time.LocalDate;
import java.time.LocalTime;

public class DateUtil {
    public static final LocalDate CURRENT_DATE = LocalDate.now();

    public static final LocalTime VOTE_TIME = LocalTime.of(11, 0, 0);

    public static final LocalTime VOTE_BEFORE_TIME = LocalTime.of(10, 0, 0);

    public static final LocalTime VOTE_AFTER_TIME = LocalTime.of(12, 0, 0);
}
