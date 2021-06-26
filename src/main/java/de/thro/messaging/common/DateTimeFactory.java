package de.thro.messaging.common;

import java.time.LocalDateTime;

public class DateTimeFactory {
    public static LocalDateTime getDateTime() {
        return LocalDateTime.now();
    }
}
