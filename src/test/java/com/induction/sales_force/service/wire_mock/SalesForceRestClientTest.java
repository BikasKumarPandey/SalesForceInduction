package com.induction.sales_force.service.wire_mock;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.induction.sales_force.dto.AccessTokenResponse;
import com.induction.sales_force.service.rest_api.SalesForceRestClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.induction.sales_force.util.ApplicationConstants.SALES_FORCE_TOKEN_URL;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

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

        // Create a RestTemplate with custom configuration (optional)
        restTemplate = new RestTemplateBuilder().rootUri("http://localhost:9090").build();
    }

    @AfterEach
    public void tearDown() {
        wireMockServer.stop();
    }

    @Test
    public void getToken_when_Invalid_input_throws_exception() {
        wireMockServer.stubFor(post(urlEqualTo("/services/oauth2/token"))
                .withHeader("Accept", equalTo("application/json"))
                .willReturn(aResponse()
                        .withStatus(400) // Simulate a 400 Bad Request
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"error\":\"invalid_grant\",\"error_description\":\"authentication failure\"}")
                ));

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<String> httpEntity = new HttpEntity<>("grant_type=password&client_id=asgdghdf&client_secret=sadgagsd&username=username&password=password", httpHeaders);

        assertThrows(HttpClientErrorException.BadRequest.class, () -> {
            restTemplate.exchange(SALES_FORCE_TOKEN_URL, HttpMethod.POST, httpEntity, String.class);
        });
    }

    @Test
    public void testGetTokenSuccess() {
        wireMockServer.stubFor(post(urlEqualTo("/services/oauth2/token"))
                .withHeader("Content-Type", equalTo(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .willReturn(aResponse()
                        .withStatus(200) // Simulate a 200 OK
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"access_token\": \"valid-access-token\"}")
                ));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<String> httpEntity = new HttpEntity<>("grant_type=password&client_id=asgdghdf&client_secret=sadgagsd&username=username&password=password", httpHeaders);

        doReturn("http://localhost:9090/services/oauth2/token").when(salesForceRestClient).getSalesForceTokenUrl();
        AccessTokenResponse token = salesForceRestClient.getToken(httpEntity);

    }

}
