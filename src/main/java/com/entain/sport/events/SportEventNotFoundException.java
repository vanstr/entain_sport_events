package com.entain.sport.events;

public class SportEventNotFoundException extends RuntimeException {

    public SportEventNotFoundException(String message) {
        super(message);
    }

    public SportEventNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
