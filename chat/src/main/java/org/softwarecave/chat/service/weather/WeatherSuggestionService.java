package org.softwarecave.chat.service.weather;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.softwarecave.chat.service.config.ChatOptionsFactory;
import org.softwarecave.chat.service.weather.client.CurrentWeatherFormatter;
import org.softwarecave.chat.service.weather.client.WeatherClient;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

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

    public Mono<WeatherSuggestionResponse> getClothingSuggestion(double latitude, double longitude) {
        var currentWeatherMono = weatherClient.getCurrent(latitude, longitude);

        return currentWeatherMono.map(currentWeather -> {
            var currentWeatherText = CurrentWeatherFormatter.format(currentWeather);
            log.info("Current weather: {}", currentWeatherText);

            try {
                var result = chatClient
                        .prompt()
                        .options(chatOptionsFactory.create(300, 1.0))
                        .system(SYSTEM_PROMPT)
                        .user(currentWeatherText)
                        .call()
                        .entity(WeatherSuggestionResponse.class);
                log.info("Suggestion: {}", result);
                return result;
            } catch (Exception e) {
                log.error("Failed to get clothing suggestion from LLM", e);
                throw new WeatherProcessingException("Failed to get clothing suggestion from LLM", e);
            }
        });
    }
}

