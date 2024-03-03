package config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.*;

@Configuration
public class TestConfig {

    @Bean
    public Clock fixedClock() {
        LocalTime fixedTime = LocalTime.of(10, 0);
        Instant fixedInstant = LocalDateTime.of(LocalDate.now(), fixedTime).toInstant(ZoneOffset.UTC);
        return Clock.fixed(fixedInstant, ZoneOffset.UTC);
    }
}