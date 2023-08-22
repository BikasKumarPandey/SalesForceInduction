package com.induction.sales_force.service.wire_mock;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.induction.sales_force.util.ApplicationConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SalesForceRestClientTest {

    private WireMockServer wireMockServer;
    private RestTemplate restTemplate;

    @BeforeEach
    public void setUp() {
        // Start WireMock on a specific port (e.g., 9090)
        wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig()
                .port(9090)
                .notifier(new ConsoleNotifier(true))); // Optional, for debugging

        wireMockServer.start();

        // Create a RestTemplate with custom configuration (optional)
        restTemplate = new RestTemplateBuilder().rootUri("http://localhost:9090").build();
    }

    @AfterEach
    public void tearDown() {
        // Stop WireMock after each test
        wireMockServer.stop();
    }

    @Test
    public void getToken_when_Invalid_input_throws_exception() {
        // Define a WireMock stub for the Salesforce token endpoint
        wireMockServer.stubFor(post(urlEqualTo("/services/oauth2/token"))
                .withHeader("Accept", equalTo("application/json"))
                .willReturn(aResponse()
                        .withStatus(400) // Simulate a 400 Bad Request
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"error\":\"invalid_grant\",\"error_description\":\"authentication failure\"}")
                ));

        // Make a POST request to the mock Salesforce token endpoint
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<String> httpEntity = new HttpEntity<>("grant_type=password&client_id=asgdghdf&client_secret=sadgagsd&username=username&password=password", httpHeaders);

        // The restTemplate.exchange() call will throw a HttpClientErrorException$BadRequest
        // because the response is configured to be a 400 Bad Request with an "invalid_grant" error.
        assertThrows(HttpClientErrorException.BadRequest.class, () -> {
            restTemplate.exchange(SALES_FORCE_TOKEN_URL, HttpMethod.POST, httpEntity, String.class);
        });

        // Add assertions for your specific error handling logic if needed.
    }

}
