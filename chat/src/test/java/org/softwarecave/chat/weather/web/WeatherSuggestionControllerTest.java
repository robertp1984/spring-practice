package org.softwarecave.chat.weather.web;

import org.junit.jupiter.api.Test;
import org.softwarecave.chat.weather.service.WeatherSuggestionResponse;
import org.softwarecave.chat.weather.service.WeatherSuggestionService;
import org.softwarecave.chat.weather.web.WeatherSuggestionController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WeatherSuggestionController.class)
@ActiveProfiles("test")
class WeatherSuggestionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private WeatherSuggestionService weatherSuggestionService;

    @Test
    void testGetSuggestionSuccess() throws Exception {
        // Arrange
        double latitude = 10;
        double longitude = -10;
        List<String> weatherLines = List.of("Temperature: 25°C", "Rain: 5mm");
        List<String> locationLines = List.of("Location: Sample City");
        String clothingSuggestion = "Wear light clothing and sunscreen.";
        WeatherSuggestionResponse response = new WeatherSuggestionResponse(weatherLines, locationLines, clothingSuggestion);

        when(weatherSuggestionService.getClothingSuggestion(latitude, longitude))
                .thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/api/v1/weatherSuggestion")
                        .param("latitude", String.valueOf(latitude))
                        .param("longitude", String.valueOf(longitude))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().string(containsString("Current weather:")))
                .andExpect(content().string(containsString("Temperature: 25°C")))
                .andExpect(content().string(containsString("Rain: 5mm")))
                .andExpect(content().string(containsString("Location: Sample City")))
                .andExpect(content().string(containsString("Suggestion:")))
                .andExpect(content().string(containsString("Wear light clothing and sunscreen.")));

        verify(weatherSuggestionService, times(1)).getClothingSuggestion(latitude, longitude);
    }

    @Test
    void testGetSuggestionWithEmptyWeatherLines() throws Exception {
        // Arrange
        double latitude = 35.6762;
        double longitude = 139.6503;
        List<String> locationLines = List.of("Location: Sample City");
        WeatherSuggestionResponse response = new WeatherSuggestionResponse(List.of(), locationLines, "No weather data available.");

        when(weatherSuggestionService.getClothingSuggestion(latitude, longitude))
                .thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/api/v1/weatherSuggestion")
                        .param("latitude", String.valueOf(latitude))
                        .param("longitude", String.valueOf(longitude))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Current weather:")))
                .andExpect(content().string(containsString("Location: Sample City")))
                .andExpect(content().string(containsString("Suggestion:")))
                .andExpect(content().string(containsString("No weather data available.")));
    }

    @Test
    void testGetSuggestionInvokesServiceWithCorrectParams() throws Exception {
        // Arrange
        double latitude = 37.7749;
        double longitude = -122.4194;
        List<String> locationLines = List.of("Location: San Francisco, CA");
        WeatherSuggestionResponse response = new WeatherSuggestionResponse(
                List.of("Temperature: 22°C"),
                locationLines,
                "Great weather for outdoor activities."
        );

        when(weatherSuggestionService.getClothingSuggestion(latitude, longitude))
                .thenReturn(response);

        // Act
        mockMvc.perform(get("/api/v1/weatherSuggestion")
                        .param("latitude", String.valueOf(latitude))
                        .param("longitude", String.valueOf(longitude))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Assert
        verify(weatherSuggestionService, times(1)).getClothingSuggestion(latitude, longitude);
    }

    @Test
    void testGetSuggestionContentType() throws Exception {
        // Arrange
        double latitude = 52.5200;
        double longitude = 13.4050;
        List<String> locationLines = List.of("Location: Berlin, Germany");
        WeatherSuggestionResponse response = new WeatherSuggestionResponse(
                List.of("Temperature: 18°C"),
                locationLines,
                "Wear layers for temperature changes.");

        when(weatherSuggestionService.getClothingSuggestion(latitude, longitude))
                .thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/api/v1/weatherSuggestion")
                        .param("latitude", String.valueOf(latitude))
                        .param("longitude", String.valueOf(longitude))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    void testGetSuggestionWithDecimalCoordinates() throws Exception {
        // Arrange
        double latitude = 44.4268;
        double longitude = 26.1025;
        List<String> weatherLines = List.of("Temperature: 16°C");
        List<String> locationLines = List.of("Location: Bucharest, Romania");
        String clothingSuggestion = "Bring a light jacket.";
        WeatherSuggestionResponse response = new WeatherSuggestionResponse(weatherLines, locationLines, clothingSuggestion);

        when(weatherSuggestionService.getClothingSuggestion(latitude, longitude))
                .thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/api/v1/weatherSuggestion")
                        .param("latitude", String.valueOf(latitude))
                        .param("longitude", String.valueOf(longitude))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("light jacket")));

        verify(weatherSuggestionService, times(1)).getClothingSuggestion(latitude, longitude);
    }

    @Test
    void testGetSuggestionIncludesFormattedStructure() throws Exception {
        // Arrange
        double latitude = 10.0;
        double longitude = 12.00;
        List<String> weatherLines = List.of("Temperature: 15°C", "Rain: 20mm");
        List<String> locationLines = List.of("Location: Sample City");
        String clothingSuggestion = "Wear light clothing and take umbrella.";
        WeatherSuggestionResponse response = new WeatherSuggestionResponse(weatherLines, locationLines, clothingSuggestion);

        when(weatherSuggestionService.getClothingSuggestion(latitude, longitude))
                .thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/api/v1/weatherSuggestion")
                        .param("latitude", String.valueOf(latitude))
                        .param("longitude", String.valueOf(longitude))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Current weather:")))
                .andExpect(content().string(containsString("Temperature: 15°C")))
                .andExpect(content().string(containsString("Rain: 20mm")))
                .andExpect(content().string(containsString("Location: Sample City")))
                .andExpect(content().string(containsString("Suggestion:")))
                .andExpect(content().string(containsString("Wear light clothing and take umbrella.")));
    }
}
