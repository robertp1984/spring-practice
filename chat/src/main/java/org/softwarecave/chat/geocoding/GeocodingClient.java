package org.softwarecave.chat.geocoding;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
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

    List<GeocodingLocation> getGeocodingLocations(String city, String country) {
        RestClient restClient = getRestClient();

        return restClient.get()
                .uri(geocodingUrl, city, country)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

    List<GeocodingLocation> getReverseGeocodingLocations(double latitude, double longitude) {
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
