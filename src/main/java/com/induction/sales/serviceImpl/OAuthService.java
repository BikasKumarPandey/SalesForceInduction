package com.induction.sales.serviceImpl;

import com.induction.sales.util.AccessTokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OAuthService {

    @Value("${salesforce.consumerKey}")
    private String consumerKey;

    @Value("${salesforce.consumerSecret}")
    private String consumerSecret;

    public String getAccessToken(String username, String password) {
        RestTemplate restTemplate = new RestTemplate();
        String tokenUrl = "https://login.salesforce.com/services/oauth2/token";
        String requestBody = "grant_type=password"
                + "&client_id=" + consumerKey
                + "&client_secret=" + consumerSecret
                + "&username=" + username
                + "&password=" + password;

        AccessTokenResponse response = restTemplate.postForObject(tokenUrl, requestBody, AccessTokenResponse.class);

        if (response != null) {
            return response.getAccessToken();
        }

        return null;
    }
}
