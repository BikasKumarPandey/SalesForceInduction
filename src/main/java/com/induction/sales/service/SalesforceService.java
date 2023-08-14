package com.induction.sales.service;

import com.induction.sales.dto.Event;
import org.springframework.http.ResponseEntity;

public interface SalesforceService {

    String getSalesforceToken(String userName, String password) throws Exception;

    String getEvents(String token);

    String getEventDetails();

    ResponseEntity<String> createEvent(Event event);

    ResponseEntity<String> getEventFromSalesForce(Event event);
}
