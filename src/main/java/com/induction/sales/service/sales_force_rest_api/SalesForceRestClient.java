package com.induction.sales.service.sales_force_rest_api;

import com.induction.sales.dto.AccessTokenResponse;
import com.induction.sales.dto.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpMethod;

import static com.induction.sales.util.ApplicationConstants.SALES_FORCE_TOKEN_URL;
import static com.induction.sales.util.ApplicationConstants.SALES_FORCE_GET_EVENT_URL;
import static com.induction.sales.util.ApplicationConstants.SALES_FORCE_CREATE_EVENT_URL;

/**
 * A service class for interacting with the SalesForce REST API.
 */

@Service
public class SalesForceRestClient {

    @Autowired
    public RestTemplate restTemplate;

    private static final String REST_TEMPLATE_ERROR = "Invalid Credentials or Error while communicating with Sales force url";

    /**
     * Retrieves an access token from the SalesForce API.
     *
     * @param entity The HTTP request entity containing the User credential and content type.
     * @return An AccessTokenResponse containing the access token to perform operation such as events.
     * @throws Exception If an error occurs during the API call.
     */
// TODO: 17/08/23 5. use httpclient , 1. throw specific error, 1. wire mock
    public AccessTokenResponse getToken(HttpEntity<String> entity) throws Exception {
        ResponseEntity<AccessTokenResponse> salesForceToken;
        try {
            salesForceToken = new ResponseEntity<>(restTemplate.postForObject(SALES_FORCE_TOKEN_URL, entity, AccessTokenResponse.class), HttpStatus.OK);
        } catch (Exception e) {
            throw new Exception(REST_TEMPLATE_ERROR);
        }
        return salesForceToken.getBody();
    }

    /**
     * Creates an event in the SalesForce system.
     *
     * @param requestEntity The HTTP request entity containing the event information and headers.
     * @return A ResponseEntity containing the response from the SalesForce API.
     * @throws Exception If an error occurs during the API call.
     */
    public ResponseEntity<String> createEventInSalesForce(HttpEntity<Event> requestEntity) throws Exception {
        ResponseEntity<String> createdEvent;
        try {
            createdEvent = restTemplate.exchange(SALES_FORCE_CREATE_EVENT_URL, HttpMethod.POST, requestEntity, String.class);
        } catch (Exception e) {
            throw new Exception(REST_TEMPLATE_ERROR);
        }
        return createdEvent;
    }

    /**
     * Retrieves an event from the SalesForce system.
     *
     * @param requestEntity The HTTP request entity containing the header information .
     * @return A ResponseEntity containing the response from the SalesForce API.
     * @throws Exception If an error occurs during the API call.
     */
    public ResponseEntity<String> getEventFromSalesForce(HttpEntity<Event> requestEntity) throws Exception {
        ResponseEntity<String> eventsFromSalesForce;
        try {
            eventsFromSalesForce = restTemplate.exchange(SALES_FORCE_GET_EVENT_URL, HttpMethod.GET, requestEntity, String.class);
        } catch (Exception e) {
            throw new Exception(REST_TEMPLATE_ERROR);
        }
        return eventsFromSalesForce;
    }

}
