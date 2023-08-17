package com.induction.sales.service.service_impl;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.induction.sales.dto.Event;
import com.induction.sales.service.sales_force_rest_api.SalesForceRestClient;
import com.induction.sales.service.SalesforceService;
import com.induction.sales.dto.AccessTokenResponse;

import static com.induction.sales.util.ApplicationConstants.GRANT_TYPE;
import static com.induction.sales.util.ApplicationConstants.CLIENT_ID;
import static com.induction.sales.util.ApplicationConstants.CLIENT_SECRET;
import static com.induction.sales.util.ApplicationConstants.USER_NAME_KEY;
import static com.induction.sales.util.ApplicationConstants.PASSWORD_KEY;
import static com.induction.sales.util.ApplicationConstants.BEARER;

@Service
public class SalesforceServiceImpl implements SalesforceService {

    private static final Logger logger = LoggerFactory.getLogger(SalesforceServiceImpl.class);

    @Value("${salesforce.consumerKey}")
    private String consumerKey;

    @Value("${salesforce.consumerSecret}")
    private String consumerSecret;

    @Autowired
    private SalesForceRestClient salesForceRestClient;

    @Override
    public String getSalesforceToken(String userName, String userPassword) throws Exception {
        if ((userName.isBlank() || userName.isEmpty() || userName == null) ||
                (userPassword.isBlank() || userPassword.isEmpty() || userPassword == null)) {
            throw new Exception("Invalid username and password");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<String> entity = new HttpEntity<>(requestBody(userName, userPassword), headers);

        logger.info("Token requested from Salesforce");
        AccessTokenResponse response = salesForceRestClient.getToken(entity);
        logger.info("AccessToken fetched successfully");

        if (response == null) {
            logger.error("Got null as response while fetching token from sales force url");
            throw new Exception("Access token is null");
        }
        return response.getAccessToken();
    }

    public ResponseEntity<String> createEventInSalesForce(Event event, String authorizationHeader) throws Exception {
        if (event == null) {
            logger.error("event details are not added.");
            throw new Exception("Required details not added");
        }
        String accessToken = authorizationHeader.replace(BEARER, "");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", BEARER + accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Event> requestEntity = new HttpEntity<>(event, headers);

        logger.info("Requested to create event in SalesForce");
        ResponseEntity<String> eventInSalesForce = salesForceRestClient.createEventInSalesForce(requestEntity);
        logger.info("Event created successfully in SalesForce");

        if (eventInSalesForce == null) {
            logger.error("could not create event in sales force.");
            throw new Exception("Event not created");
        }
        return eventInSalesForce;
    }

    public ResponseEntity<String> getEventFromSalesForce(String authorizationHeader) throws Exception {
        String accessToken = authorizationHeader.replace(BEARER, "");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", BEARER + accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Event> requestEntity = new HttpEntity<>(headers);

        logger.info("Requested Events from SalesForce.");
        ResponseEntity<String> eventFromSalesForce = salesForceRestClient.getEventFromSalesForce(requestEntity);
        logger.info("Fetched Events from SalesForce successfully.");

        if (eventFromSalesForce == null) {
            logger.error("No Event found,");
            throw new Exception("No Event found.");
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
