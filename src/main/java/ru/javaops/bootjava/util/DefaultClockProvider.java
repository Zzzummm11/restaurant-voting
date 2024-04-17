package ru.javaops.bootjava.util;

import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class DefaultClockProvider implements ClockProvider {

    @Override
    public LocalTime getTime() {
        return LocalTime.now();
    }
}
