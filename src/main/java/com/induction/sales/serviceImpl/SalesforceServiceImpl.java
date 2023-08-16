package com.induction.sales.serviceImpl;

import com.induction.sales.dto.Event;
import com.induction.sales.serviceImpl.SalesForceMsCommunication.SalesForceRestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.induction.sales.service.SalesforceService;
import org.springframework.beans.factory.annotation.Value;
import com.induction.sales.dto.AccessTokenResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpMethod;

import static com.induction.sales.util.ApplicationConstants.*;

@Service
public class SalesforceServiceImpl implements SalesforceService {

    private static final Logger logger = LoggerFactory.getLogger(SalesforceServiceImpl.class);

    @Value("${salesforce.consumerKey}")
    private String consumerKey;

    @Value("${salesforce.consumerSecret}")
    private String consumerSecret;

    @Value("${salesforce.redirectUri}")
    private String redirectUri;

    @Value("${salesforce.api.access-token}")
    private String salesforceAccessToken;

    @Autowired
    private SalesForceRestClient salesForceRestClient;

    @Override
    public String getSalesforceToken(String userName, String userPassword) throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> entity = new HttpEntity<>(requestBody(userName, userPassword), headers);

        logger.info("Token requested from Salesforce");
        AccessTokenResponse response = salesForceRestClient.getToken(entity);
        logger.info("AccessToken fetched successfully");

        if (response == null) {
            logger.error("Got null as response while fetching token from sales force url");
            throw new Exception("Access token is null");
        }

        return response.getAccessToken();
    }

    public ResponseEntity<String> createEvent(Event event) {
        String baseUrl = "https://sacumen7-dev-ed.develop.my.salesforce.com";

        String trying = baseUrl + "/services/data/v53.0/sobjects/Event/";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + salesforceAccessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Event> requestEntity = new HttpEntity<>(event, headers);

        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(trying, HttpMethod.POST, requestEntity, String.class);
    }

    public ResponseEntity<String> getEventFromSalesForce(Event event) {
        String apiUrl = "https://sacumen7-dev-ed.develop.my.salesforce.com/services/data/v58.0/query?q=SELECT Id, Subject, StartDateTime, EndDateTime FROM Event";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + salesforceAccessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Event> requestEntity = new HttpEntity<>(event, headers);

        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(apiUrl, HttpMethod.GET, requestEntity, String.class);
    }

    private String requestBody(String username, String userPassword) {
        return grantType
                + clientId + "=" + consumerKey
                + clientSecret + "=" + consumerSecret
                + userName + "=" + username
                + password + "=" + userPassword;
    }


}
