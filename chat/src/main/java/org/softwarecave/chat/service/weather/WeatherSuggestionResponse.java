package org.softwarecave.chat.service.weather;

import java.util.List;

public record WeatherSuggestionResponse(
        List<String> currentWeatherLines,
        List<String> locationLines,
        String clothingSuggestion
) {
}
