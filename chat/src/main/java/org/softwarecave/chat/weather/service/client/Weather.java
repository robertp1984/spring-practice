package org.softwarecave.chat.weather.service.client;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public record Weather(@JsonProperty("current_units") CurrentUnits currentUnits,
                      @JsonProperty("current") Current current)
        implements Serializable {

    public record CurrentUnits(
            @JsonProperty("temperature_2m") String temperature2m,
            @JsonProperty("rain") String rain,
            @JsonProperty("wind_speed_10m") String windSpeed10m) implements Serializable {
    }

    public record Current(
            @JsonProperty("temperature_2m") Double temperature2m,
            @JsonProperty("rain") Double rain,
            @JsonProperty("wind_speed_10m") Double windSpeed10m) implements Serializable {
    }
}
