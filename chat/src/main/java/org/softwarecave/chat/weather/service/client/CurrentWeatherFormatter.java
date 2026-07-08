package org.softwarecave.chat.weather.service.client;

import java.util.Locale;

public class CurrentWeatherFormatter {
    public static String format(Weather weather, Double latitude, Double longitude) {
        return String.format(Locale.ROOT,
                """
                        Temperature: %f %s
                        Rain: %f %s
                        Wind speed: %f %s
                        Latitude: %f
                        Longitude: %f
                        """,
                weather.current().temperature2m(), weather.currentUnits().temperature2m(),
                weather.current().rain(), weather.currentUnits().rain(),
                weather.current().windSpeed10m(), weather.currentUnits().windSpeed10m(),
                latitude, longitude);
    }
}
