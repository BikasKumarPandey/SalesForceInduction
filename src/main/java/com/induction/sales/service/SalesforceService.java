package com.induction.sales.service;

import com.induction.sales.dto.Event;
import org.springframework.http.ResponseEntity;

public interface SalesforceService {

    String getSalesforceToken(String userName, String password) throws Exception;

    ResponseEntity<String> createEventInSalesForce(Event event, String authorizationHeader) throws Exception;

    ResponseEntity<String> getEventFromSalesForce(String authorizationHeader) throws Exception;
}
