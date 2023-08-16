package com.induction.sales.serviceImpl.SalesForceMsCommunication;

import com.induction.sales.dto.AccessTokenResponse;
import com.induction.sales.dto.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
            throw new Exception("Error while communicating with Sales force url");
        }
        return salesForceToken.getBody();
    }


    public ResponseEntity<String> createEventInSalesForce(String createSalesForceEventURl, HttpEntity<Event> requestEntity) throws Exception {
        ResponseEntity<String> createdEvent = null;
                try {
                    createdEvent=  restTemplate.exchange(createSalesForceEventURl, HttpMethod.POST, requestEntity, String.class);
                }catch (Exception e) {
                    throw new Exception("Error while communicating with Sales force url");
                }
                return createdEvent;
    }
}
