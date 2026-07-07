package org.softwarecave.chat.geocoding;

public record GeocodingLocation(String name,
                                Double latitude,
                                Double longitude,
                                String country,
                                String state) {
}
