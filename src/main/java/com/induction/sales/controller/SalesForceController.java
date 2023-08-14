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



    @PostMapping("/create-event")
    public ResponseEntity<String> createEvent(@RequestBody Event event) {
        event.setSubject("Important Meeting");
        event.setStartDateTime("2023-08-15T10:00:00Z");
        event.setEndDateTime("2023-08-15T12:00:00Z");
        event.setDurationInMinutes(120); // Set the duration in minutes
        event.setActivityDateTime("2023-08-15T10:00:00Z"); // Set the activity date and time

        return salesforceService.createEvent(event);
    }

    @GetMapping("/create-event")
    public ResponseEntity<String> getEventFromSalesForce(@RequestBody Event event) {
        return salesforceService.getEventFromSalesForce(event);
    }







    @GetMapping("/events")
    public String getEvents() {
        return salesforceService.getEventDetails();
    }



 /* @GetMapping("/getEvent")
    public ResponseEntity<String> getEvents(@RequestParam("token")String token) throws Exception {
        return new ResponseEntity<>(salesforceService.getEvents(token), HttpStatus.OK);
    }*/

    @GetMapping("/getEvent")
    public ResponseEntity<String> getEvents(@RequestParam("token") String token) {
        try {
            String response = salesforceService.getEvents(token);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/callback")
    public String getSalesforceData() {
        return "call back page";
    }



@GetMapping("/api")
    public String getEve(){
  return salesforceService.getEventDetails();
}









    /*
        @Value("${salesforce.consumerKey}")
    private String consumerKey;

    @Value("${salesforce.consumerSecret}")
    private String consumerSecret;

    @GetMapping("/auth")
    public ResponseEntity<String> initiateSalesforceOAuth() {
        // Construct the authorization URL and redirect the user
        String authUrl = "https://login.salesforce.com/services/oauth2/authorize?" +
                "response_type=code&client_id=" + consumerKey +
                "&redirect_uri=http://localhost:8080/salesforce/callback";

        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", authUrl)
                .build();
    }

    @GetMapping("/salesforce/callback")
    public ResponseEntity<String> salesforceCallback(@RequestParam("code") String code) {
        // Use the code to obtain an access token from Salesforce
        // Make HTTP POST request to Salesforce token endpoint

        // Process the response and obtain the access token

        return ResponseEntity.ok("Access token obtained: " + "accessToken");
    }*/
}
