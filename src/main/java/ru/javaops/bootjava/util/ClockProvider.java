package ru.javaops.bootjava.util;

import java.time.LocalTime;

public interface ClockProvider {
   LocalTime getTime();
}
