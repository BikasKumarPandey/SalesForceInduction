package com.induction.sales_force.api;

import com.induction.sales_force.service.SalesforceService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import com.induction.sales_force.dto.Event;
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

    /**
     * Retrieves an access token from the SalesForce API.
     *
     * @param userName The username for authentication.
     * @return An AccessTokenResponse containing the access token to perform operation such as events.
     */
    @GetMapping("tokenGenerator")
    public ResponseEntity<String> getSalesforceToken(@RequestHeader("userName") String userName,
                                                     @RequestHeader("password") String password) {
        return new ResponseEntity<>(salesforceService.getSalesforceToken(userName, password), HttpStatus.OK);
    }

    /**
     * create event in the SalesForce system.
     *
     * @param event to add event details in SalesForce system.
     * @param authorizationHeader The Header containing token.
     * @return An event id and success status.
     */
    @PostMapping("event")
    public ResponseEntity<String> createEventInSalesForce(@RequestBody Event event, @RequestHeader("Authorization") String authorizationHeader) {
        return salesforceService.createEventInSalesForce(event, authorizationHeader);
    }

    /**
     * get events from the SalesForce system.
     *
     * @param authorizationHeader The Header containing token.
     * @return An all events created.
     */
    @GetMapping("event")
    public ResponseEntity<String> getEventFromSalesForce(@RequestHeader("Authorization") String authorizationHeader) {
        return salesforceService.getEventFromSalesForce(authorizationHeader);
    }

}
