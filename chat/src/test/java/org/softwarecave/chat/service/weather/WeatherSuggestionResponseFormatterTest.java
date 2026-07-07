package org.softwarecave.chat.service.weather;

import org.junit.jupiter.api.Test;
import org.softwarecave.chat.weather.service.WeatherSuggestionResponse;
import org.softwarecave.chat.weather.service.WeatherSuggestionResponseFormatter;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WeatherSuggestionResponseFormatterTest {

    @Test
    void testFormatWithSingleWeatherLine() {
        // Arrange
        List<String> weatherLines = List.of("Sunny, 25°C");
        String suggestion = "Wear light clothing and sunscreen.";
        List<String> locationLines = List.of("City: Sample City");
        WeatherSuggestionResponse response = new WeatherSuggestionResponse(weatherLines, locationLines, suggestion);

        // Act
        String result = WeatherSuggestionResponseFormatter.format(response);

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("Current weather:"));
        assertTrue(result.contains("Sunny, 25°C"));
        assertTrue(result.contains("City: Sample City"));
        assertTrue(result.contains("Suggestion:"));
        assertTrue(result.contains("Wear light clothing and sunscreen."));
    }

    @Test
    void testFormatWithMultipleWeatherLines() {
        // Arrange
        List<String> weatherLines = List.of(
                "Sunny, 25°C",
                "Humidity: 45%",
                "Wind: 10 km/h"
        );
        List<String> locationLines = List.of("City: Sample City");
        String suggestion = "Wear light, breathable clothing. Bring a hat for sun protection.";
        WeatherSuggestionResponse response = new WeatherSuggestionResponse(weatherLines, locationLines, suggestion);

        // Act
        String result = WeatherSuggestionResponseFormatter.format(response);

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("Sunny, 25°C"));
        assertTrue(result.contains("Humidity: 45%"));
        assertTrue(result.contains("Wind: 10 km/h"));
        assertTrue(result.contains("City: Sample City"));
        assertTrue(result.contains("Wear light, breathable clothing. Bring a hat for sun protection."));
    }

    @Test
    void testFormatWithEmptyWeatherLines() {
        // Arrange
        List<String> weatherLines = Collections.emptyList();
        List<String> locationLines = List.of("City: Sample City");
        String suggestion = "No weather data available.";
        WeatherSuggestionResponse response = new WeatherSuggestionResponse(weatherLines, locationLines, suggestion);

        // Act
        String result = WeatherSuggestionResponseFormatter.format(response);

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("Current weather:"));
        assertTrue(result.contains("Suggestion:"));
        assertTrue(result.contains("No weather data available."));
    }

    @Test
    void testFormatWithEmptySuggestion() {
        // Arrange
        List<String> weatherLines = List.of("Rainy, 15°C");
        List<String> locationLines = List.of("City: Sample City");
        String suggestion = "";
        WeatherSuggestionResponse response = new WeatherSuggestionResponse(weatherLines, locationLines, suggestion);

        // Act
        String result = WeatherSuggestionResponseFormatter.format(response);

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("Current weather:"));
        assertTrue(result.contains("Rainy, 15°C"));
        assertTrue(result.contains("City: Sample City"));
        assertTrue(result.contains("Suggestion:"));
    }

    @Test
    void testFormatWithMultilineSuggestion() {
        // Arrange
        List<String> weatherLines = List.of("Cold, -5°C");
        List<String> locationLines = List.of("City: Sample City");
        String suggestion = "Wear heavy winter clothing.\nLayer your clothes.\nDon't forget gloves and a scarf.";
        WeatherSuggestionResponse response = new WeatherSuggestionResponse(weatherLines, locationLines, suggestion);

        // Act
        String result = WeatherSuggestionResponseFormatter.format(response);

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("Cold, -5°C"));
        assertTrue(result.contains("City: Sample City"));
        assertTrue(result.contains("Wear heavy winter clothing."));
        assertTrue(result.contains("Layer your clothes."));
        assertTrue(result.contains("Don't forget gloves and a scarf."));
    }

    @Test
    void testFormatWithLongContent() {
        // Arrange
        List<String> weatherLines = List.of(
                "Partly cloudy with a chance of rain",
                "Temperature: 18°C",
                "Humidity: 65%",
                "Wind: 12 km/h from the northeast",
                "UV Index: 3 (moderate)"
        );
        List<String> locationLines = List.of("Location: Sample City");
        String suggestion = "Wear layers that can be adjusted throughout the day. " +
                "Bring a light rain jacket just in case. Comfortable jeans or trousers would be appropriate. " +
                "Non-slip shoes recommended if walking outdoors.";
        WeatherSuggestionResponse response = new WeatherSuggestionResponse(weatherLines, locationLines, suggestion);

        // Act
        String result = WeatherSuggestionResponseFormatter.format(response);

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("Current weather:"));
        assertTrue(result.contains("Suggestion:"));
        assertTrue(result.contains("Partly cloudy with a chance of rain"));
        assertTrue(result.contains("Bring a light rain jacket just in case"));
    }

}
