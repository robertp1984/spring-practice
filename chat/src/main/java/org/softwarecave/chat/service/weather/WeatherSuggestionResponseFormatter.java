package org.softwarecave.chat.service.weather;

import java.util.stream.Collectors;

public class WeatherSuggestionResponseFormatter {
    public static String format(WeatherSuggestionResponse weatherSuggestion) {
        String section1 = weatherSuggestion.currentWeatherLines().stream()
                .collect(Collectors.joining("\n"));
        String section2 = weatherSuggestion.clothingSuggestion();

        return """
                Current weather:
                %s
                
                Suggestion:
                %s
                """.formatted(section1, section2);
    }
}
