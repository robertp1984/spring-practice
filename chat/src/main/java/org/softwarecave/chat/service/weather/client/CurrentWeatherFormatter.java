package org.softwarecave.chat.service.weather.client;

import java.util.Locale;

public class CurrentWeatherFormatter {
    public static String format(Weather weather) {
        return String.format(Locale.ROOT,
                """
                        Temperature: %f %s
                        Rain: %f %s
                        Wind speed: %f %s
                        """,
                weather.current().temperature_2m(), weather.current_units().temperature_2m(),
                weather.current().rain(), weather.current_units().rain(),
                weather.current().wind_speed_10m(), weather.current_units().wind_speed_10m());
    }
}
