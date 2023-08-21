package com.induction.sales_force.service.service_impl;

import com.induction.sales_force.util.exception.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.induction.sales_force.dto.Event;
import com.induction.sales_force.service.rest_api.SalesForceRestClient;
import com.induction.sales_force.service.SalesforceService;
import com.induction.sales_force.dto.AccessTokenResponse;

import static com.induction.sales_force.util.ApplicationConstants.GRANT_TYPE;
import static com.induction.sales_force.util.ApplicationConstants.CLIENT_ID;
import static com.induction.sales_force.util.ApplicationConstants.CLIENT_SECRET;
import static com.induction.sales_force.util.ApplicationConstants.USER_NAME_KEY;
import static com.induction.sales_force.util.ApplicationConstants.PASSWORD_KEY;
import static com.induction.sales_force.util.ApplicationConstants.BEARER;
import static com.induction.sales_force.util.ApplicationConstants.AUTHORIZATION_KEY;


/**
 * A service class implementing logic to perform sales force system operations.
 */
@Service
public class SalesforceServiceImpl implements SalesforceService {

    private static final Logger logger = LoggerFactory.getLogger(SalesforceServiceImpl.class);

    @Value("${salesforce.consumerKey}")
    private String consumerKey;

    @Value("${salesforce.consumerSecret}")
    private String consumerSecret;

    @Autowired
    private SalesForceRestClient salesForceRestClient;

    /**
     * Retrieves an access token from the SalesForce API.
     *
     * @param userName The username for authentication.
     * @param userPassword The password for authentication.
     * @return An AccessTokenResponse containing the access token to perform operation such as events.
     * @throws Exception If an error occurs during any condition pass.
     */
    @Override
    public AccessTokenResponse getSalesforceToken(String userName, String userPassword) {
        if (userName.isEmpty() || userPassword.isEmpty()) {
            logger.error("Invalid username and password");
            throw new BadRequestException("Invalid username and password");
        }
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<String> httpEntity = new HttpEntity<>(requestBody(userName, userPassword),  httpHeaders);

        logger.info("Token requested from Salesforce");
        AccessTokenResponse response = salesForceRestClient.getToken(httpEntity);
//        AccessTokenResponse response = salesForceRestClient.getToken2(userName,userPassword);
        logger.info("AccessToken fetched successfully");

//        if (response == null) {
//            logger.error("Got null as response while fetching token from sales force url");
//            throw new BadRequestException("Access token is null");
//        }
        return response;
    }

    /**
     * create event in the SalesForce system.
     *
     * @param event to add event details in SalesForce system.
     * @param authorizationHeader The Header containing token.
     * @return An event id and success status.
     * @throws Exception If an error occurs during any condition pass.
     */
    public ResponseEntity<String> createEventInSalesForce(Event event, String authorizationHeader) {
        if (event == null) {
            logger.error("event details are not added.");
            throw new BadRequestException("Required details not added");
        }
        String accessToken = authorizationHeader.replace(BEARER, "");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(AUTHORIZATION_KEY, BEARER + accessToken);
//        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Event> requestHttpEntity = new HttpEntity<>(event, httpHeaders);

        logger.info("Requested to create event in SalesForce");
        ResponseEntity<String> eventInSalesForce = salesForceRestClient.createEventInSalesForce(requestHttpEntity);
//        ResponseEntity<String> eventInSalesForce = salesForceRestClient.createEventInSalesForce2(requestHttpEntity,authorizationHeader);
        logger.info("Event created successfully in SalesForce");

        if (eventInSalesForce == null) {
            logger.error("could not create event in sales force.");
            throw new BadRequestException("Event not created");
        }
        return eventInSalesForce;
    }

    /**
     * get events from the SalesForce system.
     *
     * @param authorizationHeader The Header containing token.
     * @return An all events created.
     * @throws Exception If an error occurs during any condition pass.
     */
    public ResponseEntity<String> getEventFromSalesForce(String authorizationHeader) {
        if (authorizationHeader.isEmpty()){
            logger.error("Token not present");
            throw new BadRequestException("Token not present");
        }
        String accessToken = authorizationHeader.replace(BEARER, "");
        HttpHeaders headers = new HttpHeaders();
        headers.set(AUTHORIZATION_KEY, BEARER + accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Event> requestHttpEntity = new HttpEntity<>(headers);

        logger.info("Requested Events from SalesForce.");
        ResponseEntity<String> eventFromSalesForce = salesForceRestClient.getEventFromSalesForce(requestHttpEntity);
//        ResponseEntity<String> eventFromSalesForce = salesForceRestClient.getEventFromSalesForce2(requestHttpEntity);

        logger.info("Fetched Events from SalesForce successfully.");

        if (eventFromSalesForce == null) {
            logger.error("No Event found,");
            throw new BadRequestException("No Event found.");
        }
        return eventFromSalesForce;
    }


    private String requestBody(String username, String userPassword) {
        return GRANT_TYPE
                + CLIENT_ID + "=" + consumerKey
                + CLIENT_SECRET + "=" + consumerSecret
                + USER_NAME_KEY + "=" + username
                + PASSWORD_KEY + "=" + userPassword;
    }


}
