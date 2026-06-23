package org.softwarecave.chat.service.weather;

public class WeatherProcessingException extends RuntimeException {
    public WeatherProcessingException(String message) {
        super(message);
    }

    public WeatherProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
