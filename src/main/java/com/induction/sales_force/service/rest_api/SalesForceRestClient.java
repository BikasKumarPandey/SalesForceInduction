package com.induction.sales_force.service.rest_api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.induction.sales_force.dto.AccessTokenResponse;
import com.induction.sales_force.dto.Event;
import com.induction.sales_force.util.exception.ResourceNotFoundException;
import com.induction.sales_force.util.exception.BadRequestException;
import com.induction.sales_force.util.exception.UnauthorizedAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;


import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static com.induction.sales_force.util.ApplicationConstants.*;

/**
 * A service class for interacting with the SalesForce REST API.
 */

@Service
public class SalesForceRestClient {
    @Value("${salesforce.consumerKey}")
    private String consumerKey;

    @Value("${salesforce.consumerSecret}")
    private String consumerSecret;
    private static final Logger logger = LoggerFactory.getLogger(SalesForceRestClient.class);

    @Autowired
    public RestTemplate restTemplate;

    /**
     * Retrieves an access token from the SalesForce API.
     *
     * @param httpEntity The HTTP request entity containing the User credential and content type.
     * @return An AccessTokenResponse containing the access token to perform operation such as events.
     * @throws Exception If an error occurs during the API call.
     */
// TODO: 17/08/23 5. use httpclient , 1. wire mock
    public AccessTokenResponse getToken(HttpEntity<String> httpEntity) {
        ResponseEntity<AccessTokenResponse> salesForceToken;
        try {
            salesForceToken = restTemplate.postForEntity(SALES_FORCE_TOKEN_URL, httpEntity, AccessTokenResponse.class);
        } catch (HttpClientErrorException e) {
            logger.error("Exception occurred while communicating with sales force api");
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                String responseBody = e.getResponseBodyAsString();

                if (responseBody.contains("invalid_grant")) {
                    logger.error("Authentication failed.");
                    throw new UnauthorizedAccessException("Invalid grant: Authentication failure");
                }
                logger.error("Bad Request.");
                throw new BadRequestException("Bad Request: " + responseBody);
            } else {
                logger.error("Invalid url get token to communicate with sales force api");
                throw new ResourceNotFoundException("Error calling Salesforce API url");
            }
        }
        return salesForceToken.getBody();
    }


    /**
     * Creates an event in the SalesForce system.
     *
     * @param requestHttpEntity The HTTP request entity containing the event information and headers.
     * @return A ResponseEntity containing the response from the SalesForce API.
     * @throws Exception If an error occurs during the API call.
     */
    public ResponseEntity<String> createEventInSalesForce(HttpEntity<Event> requestHttpEntity) {
        ResponseEntity<String> createdEvent;
        try {
            createdEvent = restTemplate.exchange(SALES_FORCE_CREATE_EVENT_URL, HttpMethod.POST, requestHttpEntity, String.class);
        } catch (HttpClientErrorException e) {
            logger.error("Exception occurred while communicating with sales force api");
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                logger.error("Invalid Bearer token");
                throw new UnauthorizedAccessException("Invalid Bearer token");
            } else if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                logger.error("Invalid create event url to communicate with sales force api");
                throw new ResourceNotFoundException("Invalid sales force create event url ");
            }
            logger.error("Bad Request.");
            throw new BadRequestException("Bad Request");
        }
        return createdEvent;
    }


    /**
     * Retrieves an event from the SalesForce system.
     *
     * @param requestHttpEntity The HTTP request entity containing the header information .
     * @return A ResponseEntity containing the response from the SalesForce API.
     * @throws Exception If an error occurs during the API call.
     */
    public ResponseEntity<String> getEventFromSalesForce(HttpEntity<Event> requestHttpEntity) {
        ResponseEntity<String> eventsFromSalesForce;
        try {
            eventsFromSalesForce = restTemplate.exchange(SALES_FORCE_GET_EVENT_URL, HttpMethod.GET, requestHttpEntity, String.class);
        } catch (HttpClientErrorException e) {
            logger.error("Exception occurred while communicating with sales force api");
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                logger.error("Invalid Bearer token");
                throw new UnauthorizedAccessException("Invalid Bearer token");
            } else if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                logger.error("Invalid get event url to communicate with sales force api");
                throw new ResourceNotFoundException("Invalid sales force get event url ");
            }
            logger.error("Bad Request.");
            throw new BadRequestException("Bad Request");
        }
        return eventsFromSalesForce;
    }


    public AccessTokenResponse getToken2(String userName, String userPassword) {
        HttpClient httpClient = HttpClient.newBuilder().build();

        String requestBody = requestBody(userName, userPassword);

        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(new URI(SALES_FORCE_TOKEN_URL))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            switch (response.statusCode()) {
                case 200:
                    ObjectMapper objectMapper = new ObjectMapper();
                    String responseBody = response.body();
                    return objectMapper.readValue(responseBody, AccessTokenResponse.class);
                case 400:
                    throw new UnauthorizedAccessException("Invalid grant: Authentication failure");
                case 404:
                    throw new ResourceNotFoundException("Error calling Salesforce API url");
                default:
                    throw new RuntimeException("Unexpected response code: " + response.statusCode());
            }
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new ResourceNotFoundException("Error occurred either in URI, send, or readValue method.");
        }
    }


    // Other methods and helper functions...

    private String requestBody(String username, String userPassword) {
        return GRANT_TYPE
                + "&" + CLIENT_ID + "=" + consumerKey
                + "&" + CLIENT_SECRET + "=" + consumerSecret
                + "&" + USER_NAME_KEY + "=" + username
                + "&" + PASSWORD_KEY + "=" + userPassword;
    }
}

