package org.softwarecave.chat.service.weather;

import lombok.extern.slf4j.Slf4j;
import org.softwarecave.chat.geocoding.GeocodingTool;
import org.softwarecave.chat.service.config.ChatOptionsFactory;
import org.softwarecave.chat.service.weather.client.CurrentWeatherFormatter;
import org.softwarecave.chat.service.weather.client.WeatherClient;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;


@Service
@Slf4j
public class WeatherSuggestionService {

    private final ChatClient chatClient;
    private final WeatherClient weatherClient;
    private final ChatOptionsFactory chatOptionsFactory;
    private final String systemPrompt;
    private final GeocodingTool geocodingTool;

    public WeatherSuggestionService(ChatClient chatClient, WeatherClient weatherClient, ChatOptionsFactory chatOptionsFactory,
                                    @Value("classpath:/prompts/weather-suggestion-system-prompt.txt") Resource systemPrompt,
                                    GeocodingTool geocodingTool) {
        this.chatClient = chatClient;
        this.weatherClient = weatherClient;
        this.chatOptionsFactory = chatOptionsFactory;
        try {
            this.systemPrompt = systemPrompt.getContentAsString(StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to load weather suggestion prompt", e);
        }
        this.geocodingTool = geocodingTool;
    }

    public WeatherSuggestionResponse getClothingSuggestion(double latitude, double longitude) {
        var currentWeather = weatherClient.getCurrent(latitude, longitude);

        var userPrompt = CurrentWeatherFormatter.format(currentWeather, latitude, longitude);
        log.info("Current weather: {}", userPrompt);

        try {
            var result = chatClient
                    .prompt()
                    .options(chatOptionsFactory.create(300, 1.0))
                    .system(systemPrompt)
                    .user(userPrompt)
                    .tools(geocodingTool)
                    .call()
                    .entity(WeatherSuggestionResponse.class);
            log.info("Suggestion: {}", result);
            return result;
        } catch (Exception e) {
            log.error("Failed to get clothing suggestion from LLM", e);
            throw new WeatherProcessingException("Failed to get clothing suggestion from LLM", e);
        }
    }
}

