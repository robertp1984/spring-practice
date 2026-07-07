package org.softwarecave.chat.weather.service;

public class WeatherProcessingException extends RuntimeException {
    public WeatherProcessingException(String message) {
        super(message);
    }

    public WeatherProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
