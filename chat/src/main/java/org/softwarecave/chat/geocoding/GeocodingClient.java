package org.softwarecave.chat.geocoding;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
@Slf4j
public class GeocodingClient {

    private final String geocodingUrl;
    private final String reverseGeocodingUrl;
    private final String apiKey;
    private final RestClient.Builder restClientBuilder;

    public GeocodingClient(@Value("${app.api-ninjas.geocoding.url}") String geocodingUrl,
                           @Value("${app.api-ninjas.reverse-geocoding.url}") String reverseGeocodingUrl,
                           @Value("${app.api-ninjas.api-key}") String apiKey,
                           RestClient.Builder restClientBuilder) {
        this.geocodingUrl = geocodingUrl;
        this.reverseGeocodingUrl = reverseGeocodingUrl;
        this.apiKey = apiKey;
        this.restClientBuilder = restClientBuilder;
    }

    @Cacheable(value = "geocodingLocations")
    public List<GeocodingLocation> getGeocodingLocations(String city, String country) {
        log.info("Getting geocoding location for city={} country={}", city, country);
        RestClient restClient = getRestClient();

        return restClient.get()
                .uri(geocodingUrl, city, country)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

    @Cacheable(value = "reverseGeocodingLocations")
    public List<GeocodingLocation> getReverseGeocodingLocations(double latitude, double longitude) {
        log.info("Getting reverse geocoding location for latitude={} longitude={}", latitude, longitude);
        RestClient restClient = getRestClient();

        return restClient.get()
                .uri(reverseGeocodingUrl, latitude, longitude)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

    private RestClient getRestClient() {
        return restClientBuilder
                .defaultHeader("X-Api-Key", apiKey)
                .build();
    }
}
