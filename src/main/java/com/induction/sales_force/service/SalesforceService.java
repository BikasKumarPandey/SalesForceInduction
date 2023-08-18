package com.induction.sales_force.service;

import com.induction.sales_force.dto.AccessTokenResponse;
import com.induction.sales_force.dto.Event;
import org.springframework.http.ResponseEntity;

public interface SalesforceService {

    AccessTokenResponse getSalesforceToken(String userName, String password);

    ResponseEntity<String> createEventInSalesForce(Event event, String authorizationHeader);

    ResponseEntity<String> getEventFromSalesForce(String authorizationHeader);
}
