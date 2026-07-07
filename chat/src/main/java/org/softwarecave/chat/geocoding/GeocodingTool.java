package org.softwarecave.chat.geocoding;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GeocodingTool {

    private final GeocodingClient geocodingClient;

    @Tool(description = "Get the geocoding location for a given city and country")
    public GeocodingLocation getGeocodingLocation(@ToolParam(description = "City name") String city,
                                                  @ToolParam(description = "Country name") String country) {
        List<GeocodingLocation> locations = geocodingClient.getGeocodingLocations(city, country);

        if (locations == null || locations.isEmpty()) {
            throw new GeocodingException("No geocoding location found for city: " + city + ", country: " + country);
        } else {
            return locations.getFirst();
        }
    }

    @Tool(description = "Get the reverse geocoding location for a given latitude and longitude")
    public GeocodingLocation getReverseGeocodingLocation(@ToolParam(description = "Latitude") double latitude,
                                                         @ToolParam(description = "Longitude") double longitude) {
        List<GeocodingLocation> locations = geocodingClient.getReverseGeocodingLocations(latitude, longitude);

        if (locations == null || locations.isEmpty()) {
            throw new GeocodingException("No reverse geocoding location found for latitude: " + latitude + ", longitude: " + longitude);
        } else {
            return locations.getFirst();
        }
    }

}
