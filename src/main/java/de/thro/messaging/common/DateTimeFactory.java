package de.thro.messaging.common;

import java.time.LocalDateTime;

public class DateTimeFactory implements IDateTimeFactory{
    public LocalDateTime getDateTime() {
        return LocalDateTime.now();
    }
}
