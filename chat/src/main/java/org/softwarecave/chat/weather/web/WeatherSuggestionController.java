package org.softwarecave.chat.weather.web;

import lombok.RequiredArgsConstructor;
import org.softwarecave.chat.weather.service.WeatherSuggestionResponseFormatter;
import org.softwarecave.chat.weather.service.WeatherSuggestionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/weatherSuggestion")
@RequiredArgsConstructor
public class WeatherSuggestionController {

    private final WeatherSuggestionService weatherSuggestionService;

    @GetMapping
    public String getSuggestion(@RequestParam("latitude") double latitude,
                                @RequestParam("longitude") double longitude) {
        var weatherSuggestion = weatherSuggestionService.getClothingSuggestion(latitude, longitude);
        return WeatherSuggestionResponseFormatter.format(weatherSuggestion);
    }

}
