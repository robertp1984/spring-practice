package org.softwarecave.chat.service.weather.client;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class WeatherClient {

    private static final String BASE_URL = "https://api.open-meteo.com/v1/forecast?latitude={latitude}&longitude={longitude}&current=temperature_2m,rain,wind_speed_10m";

    public Mono<Weather> getCurrent(double latitude, double longitude) {
        return WebClient.builder()
                .build()
                .get()
                .uri(BASE_URL, latitude, longitude)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Weather.class);
    }
}
