package com.induction.sales.serviceImpl;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class EventFetcher {

    public static void main(String[] args) {
        String accessToken = "00D5j00000Cf29l!AQgAQOlRxSa4Jn5f4PiibmGMuWI_ieLNdc6_d9MSP8aK9kMbNlwN3WW5mxpEoG1ctdzt740uQUnTCh1N449fW93f2rxk8TCA"; // Replace with your actual access token


        String baseUrl = "https://sacumen7-dev-ed.develop.my.salesforce.com";
        String query = "SELECT Id, Subject, StartDateTime, EndDateTime FROM Event";
        String url = baseUrl + "/services/data/v58.0/query?q=" + query;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                String.class,
                headers
        );

        String responseBody = response.getBody();
        System.out.println(responseBody);
    }
}

