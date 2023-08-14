/*
package com.induction.sales.serviceImpl;

import org.springframework.stereotype.Service;
import com.induction.sales.dto.AccessTokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import org.springframework.web.client.RestTemplate;

import static com.induction.sales.util.ApplicationConstants.salesForceTokenUrl;
import static com.induction.sales.util.ApplicationConstants.grantType;
import static com.induction.sales.util.ApplicationConstants.clientId;
import static com.induction.sales.util.ApplicationConstants.clientSecret;
import static com.induction.sales.util.ApplicationConstants.userName;
import static com.induction.sales.util.ApplicationConstants.password;

@Service
public class OAuthService {

    @Value("${salesforce.consumerKey}")
    private String consumerKey;

    @Value("${salesforce.consumerSecret}")
    private String consumerSecret;

    @Value("${salesforce.redirectUri}")
    private String redirectUri;

    private static final Logger logger = LoggerFactory.getLogger(OAuthService.class);
    RestTemplate restTemplate = new RestTemplate();
    public String getAccessToken(String username, String userPassword) throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> entity = new HttpEntity<>(requestBody(username, userPassword), headers);
        logger.info("Token requested from Salesforce");
        AccessTokenResponse response = restTemplate.postForObject(salesForceTokenUrl, entity, AccessTokenResponse.class);
        logger.info("AccessToken fetched successfully");
        if (response == null) {
            throw new Exception("Access token is null");
        }

        return response.getAccessToken();
    }


}
*/
