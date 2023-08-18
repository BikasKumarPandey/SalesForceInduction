package com.induction.sales.service;

import com.induction.sales.dto.Event;
import com.induction.sales.dto.UserDetails;
import org.springframework.http.ResponseEntity;

public interface SalesforceService {

    String getSalesforceToken(String userName, String password, UserDetails userDetails);

    ResponseEntity<String> createEventInSalesForce(Event event, String authorizationHeader);

    ResponseEntity<String> getEventFromSalesForce(String authorizationHeader);
}
