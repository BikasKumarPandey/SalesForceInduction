package com.induction.sales.api;

import com.induction.sales.service.SalesforceService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import com.induction.sales.dto.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/api/salesforce/")
public class SalesForceController {

    @Autowired
    private SalesforceService salesforceService;

    @GetMapping("tokenGenerator")
    public ResponseEntity<String> getSalesforceToken(@RequestParam("userName") String userName, @RequestParam("password") String password) throws Exception {
        return new ResponseEntity<>(salesforceService.getSalesforceToken(userName, password), HttpStatus.OK);
    }

    @PostMapping("event")
    public ResponseEntity<String> createEventInSalesForce(@RequestBody Event event, @RequestHeader("Authorization") String authorizationHeader) throws Exception {
        event.setSubject("Important Meeting");
        event.setStartDateTime("2023-08-15T10:00:00Z");
        event.setEndDateTime("2023-08-15T12:00:00Z");
        event.setDurationInMinutes(120); // Set the duration in minutes
        event.setActivityDateTime("2023-08-15T10:00:00Z"); // Set the activity date and time

        return salesforceService.createEvent(event, authorizationHeader);
    }

    @GetMapping("event")
    public ResponseEntity<String> getEventFromSalesForce(@RequestHeader("Authorization") String authorizationHeader) {
        return salesforceService.getEventFromSalesForce(authorizationHeader);
    }

}
