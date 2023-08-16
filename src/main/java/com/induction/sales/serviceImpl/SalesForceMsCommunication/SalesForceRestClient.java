package com.induction.sales.serviceImpl.SalesForceMsCommunication;

import com.induction.sales.dto.AccessTokenResponse;
import com.induction.sales.dto.Event;
import com.induction.sales.dto.EventResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpMethod;

import static com.induction.sales.util.ApplicationConstants.salesForceTokenUrl;

@Service
public class SalesForceRestClient {

    @Autowired
    public RestTemplate restTemplate;

    public AccessTokenResponse getToken(HttpEntity<String> entity) throws Exception {
        ResponseEntity<AccessTokenResponse> salesForceToken;
        try {
            salesForceToken = new ResponseEntity<>(restTemplate.postForObject(salesForceTokenUrl, entity, AccessTokenResponse.class), HttpStatus.OK);
        } catch (Exception e) {
            throw new Exception("Invalid Credentials or Error while communicating with Sales force url");
        }
        return salesForceToken.getBody();
    }

    public ResponseEntity<String> createEventInSalesForce(String createSalesForceEventURl, HttpEntity<Event> requestEntity) throws Exception {
        ResponseEntity<String> createdEvent;
        try {
            createdEvent = restTemplate.exchange(createSalesForceEventURl, HttpMethod.POST, requestEntity, String.class);
        } catch (Exception e) {
            throw new Exception("Invalid Credentials or Error while communicating with Sales force url");
        }
        return createdEvent;
    }

    public ResponseEntity<String> getEventFromSalesForce(String getSalesForceEventUrl, HttpEntity<Event> requestEntity) throws Exception {
        ResponseEntity<String> eventsFromSalesForce;
        try {
            eventsFromSalesForce = restTemplate.exchange(getSalesForceEventUrl, HttpMethod.GET, requestEntity, String.class);
        } catch (Exception e) {
            throw new Exception("Invalid Credentials or Error while communicating with Sales force url");
        }
        return eventsFromSalesForce;
    }

}
