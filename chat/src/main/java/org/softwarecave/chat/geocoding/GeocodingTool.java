package org.softwarecave.chat.geocoding;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class GeocodingTool {

    private final String geocodingUrl;
    private final String reverseGeocodingUrl;
    private final String apiKey;

    public GeocodingTool(@Value("${app.api-ninjas.geocoding.url}") String geocodingUrl,
                         @Value("${app.api-ninjas.reverse-geocoding.url}") String reverseGeocodingUrl,
                         @Value("${app.api-ninjas.api-key}") String apiKey) {
        this.geocodingUrl = geocodingUrl;
        this.reverseGeocodingUrl = reverseGeocodingUrl;
        this.apiKey = apiKey;
    }

    @Tool(description = "Get the geocoding location for a given city and country")
    public GeocodingLocation getGeocodingLocation(@ToolParam(description = "City name") String city,
                                                  @ToolParam(description = "Country name") String country) {
        RestClient restClient = getRestClient();

        List<GeocodingLocation> locations = restClient.get()
                .uri(geocodingUrl, city, country)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

        if (locations == null || locations.isEmpty()) {
            throw new GeocodingException("No geocoding location found for city: " + city + ", country: " + country);
        } else {
            return locations.getFirst();
        }
    }

    @Tool(description = "Get the reverse geocoding location for a given latitude and longitude")
    public GeocodingLocation getReverseGeocodingLocation(@ToolParam(description = "Latitude") double latitude,
                                                         @ToolParam(description = "Longitude") double longitude) {
        RestClient restClient = getRestClient();

        List<GeocodingLocation> locations = restClient.get()
                .uri(reverseGeocodingUrl, latitude, longitude)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

        if (locations == null || locations.isEmpty()) {
            throw new GeocodingException("No reverse geocoding location found for latitude: " + latitude + ", longitude: " + longitude);
        } else {
            return locations.getFirst();
        }
    }

    private RestClient getRestClient() {
        return RestClient.builder()
                .defaultHeader("X-Api-Key", apiKey)
                .build();
    }
}
