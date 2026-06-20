package org.softwarecave.chat.service.weather;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.softwarecave.chat.service.config.ChatOptionsFactory;
import org.softwarecave.chat.service.weather.client.CurrentWeatherFormatter;
import org.softwarecave.chat.service.weather.client.WeatherClient;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class WeatherSuggestionService {

    private static final String SYSTEM_PROMPT = """
            Provide current weather information and suggestion what clothes should I wear for the given weather.
            The clothing suggestion should be 3 sentences long.
            """;
    private final ChatClient chatClient;
    private final WeatherClient weatherClient;
    private final ChatOptionsFactory chatOptionsFactory;

    public WeatherSuggestionResponse getClothingSuggestion(double latitude, double longitude) {
        var currentWeather = weatherClient.getCurrent(latitude, longitude);
        var currentWeatherText = CurrentWeatherFormatter.format(currentWeather.block());
        log.info("Current weather: {}", currentWeatherText);

        var result = chatClient
                .prompt()
                .options(chatOptionsFactory.create(300, 1.0))
                .system(SYSTEM_PROMPT)
                .user(currentWeatherText)
                .call()
                .entity(WeatherSuggestionResponse.class);
        log.info("Suggestion: {}", result);
        return result;
    }
}

