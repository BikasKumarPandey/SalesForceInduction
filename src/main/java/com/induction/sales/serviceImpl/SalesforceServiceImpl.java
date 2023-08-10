package com.induction.sales.serviceImpl;

import com.induction.sales.service.SalesforceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SalesforceServiceImpl implements SalesforceService {
    @Autowired
    private OAuthService oAuthService;

    @Override
    public String getSalesforceData(String userName, String password) {
        String accessToken = oAuthService.getAccessToken(userName,password);

        if (accessToken != null) {
            RestTemplate restTemplate = new RestTemplate();
            String apiUrl = "https://instance.salesforce.com/services/data/v53.0/query?q=SELECT+Id,Name+FROM+Account";
            String authorizationHeader = "Bearer " + accessToken;

            // Make API call and handle response
            String response = restTemplate.getForObject(apiUrl, String.class);

            return response;
        }

        return "Authentication failed";
    }
}
