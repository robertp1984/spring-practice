package org.softwarecave.chat.geocoding;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GeocodingToolTest {

    @Mock
    private GeocodingClient geocodingClient;

    @InjectMocks
    private GeocodingTool geocodingTool;

    @Test
    void getGeocodingLocation_returnsLocation() {
        // given
        var location = new GeocodingLocation("Test City", 1.23, 4.56, "TestCountry", "TestState");
        when(geocodingClient.getGeocodingLocations("Test City", "TestCountry")).thenReturn(List.of(location));

        // when
        GeocodingLocation result = geocodingTool.getGeocodingLocation("Test City", "TestCountry");

        // then
        assertThat(result).isEqualTo(location);
    }

    @Test
    void getGeocodingLocation_noLocation_throws() {
        when(geocodingClient.getGeocodingLocations("Missing", "Nowhere")).thenReturn(List.of());

        assertThatThrownBy(() -> geocodingTool.getGeocodingLocation("Missing", "Nowhere"))
                .isInstanceOf(GeocodingException.class)
                .hasMessageContaining("No geocoding location");
    }

    @Test
    void getReverseGeocodingLocation_returnsLocation() {
        // given
        var location = new GeocodingLocation("Reverse City", 7.89, 0.12, "RevCountry", "RevState");
        when(geocodingClient.getReverseGeocodingLocations(7.89, 0.12)).thenReturn(List.of(location));

        // when
        GeocodingLocation result = geocodingTool.getReverseGeocodingLocation(7.89, 0.12);

        // then
        assertThat(result).isEqualTo(location);
    }

    @Test
    void getReverseGeocodingLocation_noLocation_throws() {
        when(geocodingClient.getReverseGeocodingLocations(0.0, 0.0)).thenReturn(List.of());

        assertThatThrownBy(() -> geocodingTool.getReverseGeocodingLocation(0.0, 0.0))
                .isInstanceOf(GeocodingException.class)
                .hasMessageContaining("No reverse geocoding location");
    }
}
