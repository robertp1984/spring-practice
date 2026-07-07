package org.softwarecave.chat.weather.service.client;

public record Weather(CurrentUnits current_units, Current current) {

    public record CurrentUnits(String temperature_2m, String rain, String wind_speed_10m) {
    }

    public record Current(Double temperature_2m, Double rain, Double wind_speed_10m) {
    }
}
