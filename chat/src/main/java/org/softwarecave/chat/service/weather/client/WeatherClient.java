package org.softwarecave.chat.service.weather.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class WeatherClient {

    private final String url;

    public WeatherClient(@Value("${app.weather.api.url}") String url) {
        this.url = url;
    }

    public Mono<Weather> getCurrent(double latitude, double longitude) {
        return WebClient.builder()
                .build()
                .get()
                .uri(url, latitude, longitude)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Weather.class);
    }
}
