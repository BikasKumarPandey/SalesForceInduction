package com.induction.sales.controller;

import com.induction.sales.dto.Event;
import com.induction.sales.service.SalesforceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/salesforce")
public class SalesForceController {

    @Autowired
    private SalesforceService salesforceService;

    @GetMapping("/tokenGenerator")
    public ResponseEntity<String> getSalesforceToken(@RequestParam("userName") String userName, @RequestParam("password") String password) throws Exception {
        return new ResponseEntity<>(salesforceService.getSalesforceToken(userName, password), HttpStatus.OK);
    }



    @PostMapping("/event")
    public ResponseEntity<String> createEvent(@RequestBody Event event) {
        event.setSubject("Important Meeting");
        event.setStartDateTime("2023-08-15T10:00:00Z");
        event.setEndDateTime("2023-08-15T12:00:00Z");
        event.setDurationInMinutes(120); // Set the duration in minutes
        event.setActivityDateTime("2023-08-15T10:00:00Z"); // Set the activity date and time

        return salesforceService.createEvent(event);
    }

    @GetMapping("/event")
    public ResponseEntity<String> getEventFromSalesForce(@RequestBody Event event) {
        return salesforceService.getEventFromSalesForce(event);
    }

}
