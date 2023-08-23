package com.induction.sales_force.service.wire_mock;

import com.induction.sales_force.dto.AccessTokenResponse;
import com.induction.sales_force.dto.Event;
import com.induction.sales_force.service.rest_api.SalesForceRestClient;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;

import static com.induction.sales_force.util.MockModels.getEvent;
import static com.induction.sales_force.util.TestCasesConstantApp.CONTENT_TYPE;
import static com.induction.sales_force.util.TestCasesConstantApp.CONTENT_TYPE_JSON;
import static com.induction.sales_force.util.TestCasesConstantApp.BEARER_TOKEN;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.MockitoAnnotations;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;

import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static org.mockito.Mockito.doReturn;


public class SalesForceRestClientTest {

    private WireMockServer wireMockServer;
    @InjectMocks
    @Spy
    private SalesForceRestClient salesForceRestClient;
    @Spy
    private RestTemplate restTemplate;

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
        wireMockServer.stubFor(
                post(urlEqualTo("/services/oauth2/token"))
                        .withHeader(CONTENT_TYPE, equalTo(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader(CONTENT_TYPE, CONTENT_TYPE_JSON)
                                .withBody(getTokenMockResponse()))
        );

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<String> httpEntity = new HttpEntity<>(requestBody(), httpHeaders);

        doReturn("http://localhost:9090/services/oauth2/token").when(salesForceRestClient).getSalesForceTokenUrl();
        AccessTokenResponse token = salesForceRestClient.getToken(httpEntity);
        assertEquals(token.getAccessToken(), "valid-access-token");
        assertEquals(token.getTokenType(), "Bearer");
    }


    @Test
    public void getToken_when_Invalid_input_throws_exception() {
        wireMockServer.stubFor(
                post(urlEqualTo("/services/oauth2/token"))
                        .withHeader(CONTENT_TYPE, equalTo(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                        .willReturn(aResponse()
                                .withStatus(400) // Simulate a 200 OK
                                .withHeader(CONTENT_TYPE, CONTENT_TYPE_JSON)
                                .withBody("Bad Request: "))
        );

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<String> httpEntity = new HttpEntity<>(requestBody(), httpHeaders);

        HttpClientErrorException.BadRequest badRequest = assertThrows(HttpClientErrorException.BadRequest.class, () -> {
            restTemplate.exchange("http://localhost:9090/services/oauth2/token", HttpMethod.POST, httpEntity, String.class);
        });
        Assertions.assertEquals(badRequest.getResponseBodyAsString(), "Bad Request: ");
    }

    @Test
    public void createEventInSalesForce_when_valid_input_given_response() {
        wireMockServer.stubFor(
                post(urlEqualTo("/services/data/v58.0/sobjects/Event/"))
                        .withHeader(HttpHeaders.AUTHORIZATION, equalTo(BEARER_TOKEN))
                        .withHeader(CONTENT_TYPE, equalTo(MediaType.APPLICATION_JSON_VALUE))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(createEventInSalesForceMockResponse()))
        );

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.AUTHORIZATION, BEARER_TOKEN);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Event> requestHttpEntity = new HttpEntity<>(getEvent(), httpHeaders);
        doReturn("http://localhost:9090/services/data/v58.0/sobjects/Event/").when(salesForceRestClient).getForceCreateEventUrl();

        ResponseEntity result = salesForceRestClient.createEventInSalesForce(requestHttpEntity);

        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void testGetEventFromSalesForce_when_invalid_input_gives_exception() {
        wireMockServer.stubFor(
                get(urlEqualTo("/services/data/v58.0/query?q=SELECT%20Id,%20Subject,%20StartDateTime,%20EndDateTime%20FROM%20Event"))
                        .withHeader(HttpHeaders.AUTHORIZATION, equalTo(BEARER_TOKEN))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
        );

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.AUTHORIZATION, BEARER_TOKEN);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Event> requestHttpEntity = new HttpEntity<>(httpHeaders);

        doReturn("http://localhost:9090/services/data/v58.0/query?q=SELECT Id, Subject, StartDateTime, EndDateTime FROM Event").when(salesForceRestClient).getSalesForceGetEventUrl();

        ResponseEntity<String> result = salesForceRestClient.getEventFromSalesForce(requestHttpEntity);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }


    private String getTokenMockResponse() {
        return "{\"access_token\": \"valid-access-token\",\"token_type\": \"Bearer\"}";
    }

    private String requestBody() {
        return "grant_type=password&client_id=asgdghdf&client_secret=sadgagsd&username=username&password=password";
    }

    private String createEventInSalesForceMockResponse() {
        return "{\n" +
                "    \"id\": \"00U5j00000Tf3uyEAB\",\n" +
                "    \"success\": true,\n" +
                "    \"errors\": []\n" +
                "}";
    }

}
