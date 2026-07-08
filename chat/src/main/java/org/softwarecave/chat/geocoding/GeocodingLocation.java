package org.softwarecave.chat.geocoding;

import java.io.Serializable;

public record GeocodingLocation(String name,
                                Double latitude,
                                Double longitude,
                                String country,
                                String state) implements Serializable {
}
