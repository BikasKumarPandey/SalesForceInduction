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
    @Autowired
    private SalesForceRestClient salesForceRestClient;

//    @Autowired
//    private OAuthService oAuthService;

   /* @Override
    public String getSalesforceData(String userName, String password) throws Exception {
        String accessToken = oAuthService.getAccessToken(userName, password);

       *//* if (accessToken != null) {
            RestTemplate restTemplate = new RestTemplate();
            String apiUrl = "https://instance.salesforce.com/services/data/v53.0/query?q=SELECT+Id,Name+FROM+Account";
            String authorizationHeader = "Bearer " + accessToken;

            // Make API call and handle response
            System.out.println("Running");
            String response = restTemplate.getForObject(apiUrl, String.class);

            return response;
        }*//*

        return accessToken;
    }  */


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
        String query = "SELECT Id, Subject, StartDateTime, EndDateTime FROM Event";
        String url = baseUrl + "/services/data/v58.0/query?q=" + query;

        String trying = baseUrl+"/services/data/v53.0/sobjects/Event/";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + salesforceAccessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Event> requestEntity = new HttpEntity<>(event, headers);

        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(trying, HttpMethod.POST, requestEntity, String.class);
    }

    public ResponseEntity<String> getEventFromSalesForce(Event event) {
        String baseUrl = "https://sacumen7-dev-ed.develop.my.salesforce.com";
        String query = "SELECT Id, Subject, StartDateTime, EndDateTime FROM Event";
        String url = baseUrl + "/services/data/v58.0/query?q=" + query;

        String trying = baseUrl+"/services/data/v53.0/sobjects/Event/";

        String apiUrl = "https://sacumen7-dev-ed.develop.my.salesforce.com/services/data/v58.0/query?q=SELECT Id, Subject, StartDateTime, EndDateTime FROM Event";


        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + salesforceAccessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Event> requestEntity = new HttpEntity<>(event, headers);

        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(apiUrl, HttpMethod.GET, requestEntity, String.class);
    }









    public String getEvents(String token) {
        String baseUrl = "https://sacumen7-dev-ed.develop.my.salesforce.com";
        String query = "SELECT Id, Subject, StartDateTime, EndDateTime FROM Event";
        String url = baseUrl + "/services/data/v58.0/query?q=" + query;

//        HttpHeaders headers = new HttpHeaders();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> entity = new HttpEntity<>(requestBodyToGetEvent(token), headers);

        salesForceRestClient.getEventResponse(entity, token);
       /* ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                String.class,
                headers
        );*/

//        String responseBody = response.getBody();
        return "responseBody";

    }

    private String requestBody(String username, String userPassword) {
        return grantType
                + clientId + "=" + consumerKey
                + clientSecret + "=" + consumerSecret
                + userName + "=" + username
                + password + "=" + userPassword;
    }

    private String requestBodyToGetEvent(String token) {
        return "Authorization = Bearer " + token;
    }


    @Value("${salesforce.api.access-token}")
    private String salesforceAccessToken;

    public String getEventDetails() {

        String apiUrl = "https://sacumen7-dev-ed.develop.my.salesforce.com/services/data/v58.0/query?q=SELECT+Id+,+EventType+,+LogFile+,+LogDate+,+LogFileLength+FROM+EventLogFile+WHERE+LogDate+>+Yesterday+AND+EventType+=+'API'";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + salesforceAccessToken);
        try {

            ResponseEntity<String> response = new RestTemplate().exchange(apiUrl, HttpMethod.GET, null, String.class);
        } catch (Exception e) {
            System.out.printf("NOt found");
            throw new RuntimeException("Not found");
        }
        return "response.getBody()";
    }
}
