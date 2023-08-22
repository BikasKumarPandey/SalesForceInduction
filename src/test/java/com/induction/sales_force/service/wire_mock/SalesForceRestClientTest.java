package com.induction.sales_force.service.wire_mock;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.induction.sales_force.dto.AccessTokenResponse;
import com.induction.sales_force.dto.Event;
import com.induction.sales_force.service.rest_api.SalesForceRestClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.induction.sales_force.util.ApplicationConstants.*;
import static com.induction.sales_force.util.MockModels.getEvent;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SalesForceRestClientTest {

    private WireMockServer wireMockServer;
    @Spy
    private RestTemplate restTemplate;
    @InjectMocks
    @Spy
    private SalesForceRestClient salesForceRestClient;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig()
                .port(9090)
                .notifier(new ConsoleNotifier(true))); // Optional, for debugging

        wireMockServer.start();

    }

    @AfterEach
    public void tearDown() {
        wireMockServer.stop();
    }

    @Test
    public void getToken_when_valid_input_gives_response() {
        wireMockServer.stubFor(post(urlEqualTo("/services/oauth2/token"))
                .withHeader("Content-Type", equalTo(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .willReturn(aResponse()
                        .withStatus(200) // Simulate a 200 OK
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"access_token\": \"valid-access-token\",\"token_type\": \"Bearer\"}")
                ));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<String> httpEntity = new HttpEntity<>("grant_type=password&client_id=asgdghdf&client_secret=sadgagsd&username=username&password=password", httpHeaders);

        doReturn("http://localhost:9090/services/oauth2/token").when(salesForceRestClient).getSalesForceTokenUrl();
        AccessTokenResponse token = salesForceRestClient.getToken(httpEntity);
        assertEquals(token.getAccessToken(), "valid-access-token");
        assertEquals(token.getTokenType(), "Bearer");
    }

    @Test
    public void getToken_when_Invalid_input_throws_exception() {
        wireMockServer.stubFor(post(urlEqualTo("/services/oauth2/token"))
                .withHeader("Content-Type", equalTo(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .willReturn(aResponse()
                        .withStatus(400) // Simulate a 200 OK
                        .withHeader("Content-Type", "application/json")
                        .withBody("Bad Request: ")
                ));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<String> httpEntity = new HttpEntity<>("grant_type=password&client_id=asgdghdf&client_secret=sadgagsd&username=username&password=password", httpHeaders);

        assertThrows(HttpClientErrorException.BadRequest.class, () -> {
            restTemplate.exchange("http://localhost:9090/services/oauth2/token", HttpMethod.POST, httpEntity, String.class);
        });
    }

    @Test
    public void testCreateEventInSalesForce() {
        Event event = new Event("V58 httpclient", "2023-08-15T10:00:00Z", "2023-08-15T10:00:00Z", 120, "2023-08-15T12:00:00Z");
        String authorizationHeader = "Bearer 00D5j00000Cf29l!AQgAQLSGlViCQ_JxtKoPmAWw3b_viC1FmiJrmmg99CvuXhQnFMkdGf4iCmFRcXpfzz9nD1kx7uc9j54rP63yAIJu0E8awzzQ"; // Replace with a valid access token

        wireMockServer.stubFor(post(urlEqualTo("/services/data/v58.0/sobjects/Event/"))
                .withHeader(HttpHeaders.AUTHORIZATION, equalTo(authorizationHeader))
                .withHeader("Content-Type", equalTo(MediaType.APPLICATION_JSON_VALUE))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody("{\n" +
                                "    \"id\": \"00U5j00000Tf3uyEAB\",\n" +
                                "    \"success\": true,\n" +
                                "    \"errors\": []\n" +
                                "}"))
        );

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.AUTHORIZATION, authorizationHeader);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Event> requestHttpEntity = new HttpEntity<>(event, httpHeaders);
        doReturn("http://localhost:9090/services/data/v58.0/sobjects/Event/").when(salesForceRestClient).getForceCreateEventUrl();

        ResponseEntity result = salesForceRestClient.createEventInSalesForce(requestHttpEntity);

        assertEquals(HttpStatus.OK, result.getStatusCode());

    }


}
