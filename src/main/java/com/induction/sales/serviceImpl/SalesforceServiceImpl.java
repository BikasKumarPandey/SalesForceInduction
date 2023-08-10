package com.induction.sales.serviceImpl;

import com.induction.sales.service.SalesforceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SalesforceServiceImpl implements SalesforceService {

    private static final Logger logger = LoggerFactory.getLogger(SalesforceServiceImpl.class);

    @Autowired
    private OAuthService oAuthService;

    @Override
    public String getSalesforceData(String userName, String password) {
        logger.info("Running 1");

        String accessToken = oAuthService.getAccessToken(userName, password);

        if (accessToken != null) {
            RestTemplate restTemplate = new RestTemplate();
            String apiUrl = "https://instance.salesforce.com/services/data/v53.0/query?q=SELECT+Id,Name+FROM+Account";
            String authorizationHeader = "Bearer " + accessToken;

            // Make API call and handle response
            System.out.println("Running");
            String response = restTemplate.getForObject(apiUrl, String.class);

            return response;
        }

        return "Authentication failed";
    }
}
