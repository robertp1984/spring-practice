package org.softwarecave.chat.geocoding;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.restclient.test.autoconfigure.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.client.MockRestServiceServer;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(GeocodingClient.class)
@ActiveProfiles("test")
public class GeocodingClientTest {

    static final double LATITUDE = 52.2297;
    static final double LONGITUDE = 21.0122;

    @Autowired
    private GeocodingClient geocodingClient;

    @Autowired
    private MockRestServiceServer mockServer;

    @Autowired
    private JsonMapper jsonMapper;

    private String getSampleGeocodingResponse() {
        var location = new GeocodingLocation("Warsaw", LATITUDE, LONGITUDE, "Poland", "Masovian Voivodeship");
        return jsonMapper.writeValueAsString(List.of(location));
    }

    @Test
    void testGetGeocodingLocations() {
        mockServer
                .expect(requestTo(containsString("geocoding")))
                .andRespond(withSuccess().body(getSampleGeocodingResponse()).contentType(MediaType.APPLICATION_JSON));

        var locations = geocodingClient.getGeocodingLocations("Warsaw", "Poland");

        assertThat(locations).isNotNull()
                .hasSize(1);
        assertThat(locations.getFirst()).hasFieldOrPropertyWithValue("name", "Warsaw")
                .hasFieldOrPropertyWithValue("latitude", LATITUDE)
                .hasFieldOrPropertyWithValue("longitude", LONGITUDE)
                .hasFieldOrPropertyWithValue("country", "Poland");
    }

    @Test
    void testGetGeocodingLocations_emptyResponse() {
        mockServer
                .expect(requestTo(containsString("geocoding")))
                .andRespond(withSuccess().body("[]").contentType(MediaType.APPLICATION_JSON));

        var locations = geocodingClient.getGeocodingLocations("NonExistentCity", "Nowhere");

        assertThat(locations).isNotNull()
                .isEmpty();
    }

    @Test
    void testGetReverseGeocodingLocations() {
        mockServer
                .expect(requestTo(containsString("reversegeocoding")))
                .andRespond(withSuccess().body(getSampleGeocodingResponse()).contentType(MediaType.APPLICATION_JSON));

        var locations = geocodingClient.getReverseGeocodingLocations(LATITUDE, LONGITUDE);

        assertThat(locations).isNotNull()
                .hasSize(1);
        assertThat(locations.getFirst()).hasFieldOrPropertyWithValue("name", "Warsaw")
                .hasFieldOrPropertyWithValue("latitude", LATITUDE)
                .hasFieldOrPropertyWithValue("longitude", LONGITUDE)
                .hasFieldOrPropertyWithValue("country", "Poland");
    }


    @Test
    void testGetReverseGeocodingLocations_emptyResponse() {
        mockServer
                .expect(requestTo(containsString("reversegeocoding")))
                .andRespond(withSuccess().body("[]").contentType(MediaType.APPLICATION_JSON));

        var locations = geocodingClient.getReverseGeocodingLocations(0.0, 0.0);

        assertThat(locations).isNotNull()
                .isEmpty();
    }
}
