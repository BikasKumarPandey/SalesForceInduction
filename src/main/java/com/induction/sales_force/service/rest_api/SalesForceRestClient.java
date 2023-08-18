package com.induction.sales_force.service.rest_api;

import com.induction.sales_force.dto.AccessTokenResponse;
import com.induction.sales_force.dto.Event;
import com.induction.sales_force.util.exception.ResourceNotFoundException;
import com.induction.sales_force.util.exception.BadRequestException;
import com.induction.sales_force.util.exception.UnauthorizedAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpMethod;

import static com.induction.sales_force.util.ApplicationConstants.SALES_FORCE_TOKEN_URL;
import static com.induction.sales_force.util.ApplicationConstants.SALES_FORCE_GET_EVENT_URL;
import static com.induction.sales_force.util.ApplicationConstants.SALES_FORCE_CREATE_EVENT_URL;

/**
 * A service class for interacting with the SalesForce REST API.
 */

@Service
public class SalesForceRestClient {

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

}
