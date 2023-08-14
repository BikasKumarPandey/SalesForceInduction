package com.induction.sales.serviceImpl.SalesForceMsCommunication;

import com.induction.sales.dto.AccessTokenResponse;
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
            throw new Exception("Error while commincating with Sales force url");
        }
        return salesForceToken.getBody();
    }


    public String getEventResponse(HttpEntity<String> entity, String token) {
        String baseUrl = "https://sacumen7-dev-ed.develop.my.salesforce.com";
        String query = "SELECT Id, Subject, StartDateTime, EndDateTime FROM Event";
        String url = baseUrl + "/services/data/v58.0/query?q=" + query;
      /*  ResponseEntity<String> response = new ResponseEntity<>(restTemplate.postForObject(url, entity, String.class), HttpStatus.OK);
        System.out.printf("Finall " + response);
        return "";*/


        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                String.class,
                headers
        );
        return "";
    }
}
