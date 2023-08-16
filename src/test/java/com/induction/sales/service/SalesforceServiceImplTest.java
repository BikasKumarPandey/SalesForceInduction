package com.induction.sales.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.induction.sales.dto.AccessTokenResponse;
import com.induction.sales.serviceImpl.SalesForceMsCommunication.SalesForceRestClient;
import com.induction.sales.serviceImpl.SalesforceServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.induction.sales.util.MockModels.getAccessTokenResponse;
import static com.induction.sales.util.MockModels.getEvent;
import static com.induction.sales.util.TestCasesConstantApp.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SalesforceServiceImplTest {

    @InjectMocks
    private SalesforceServiceImpl salesforceService;

    @Mock
    private SalesForceRestClient salesForceRestClient;

    @Test
    public void getSalesforceToken_when_userName_is_empty_throw_exception() throws Exception {

        assertThrows(Exception.class, () -> {
            salesforceService.getSalesforceToken("", passwordValue);
        });
    }

    @Test
    public void getSalesforceToken_when_password_is_empty_throw_exception() throws Exception {

        assertThrows(Exception.class, () -> {
            salesforceService.getSalesforceToken(userNameValue, "");
        });
    }

    @Test
    public void getSalesforceToken_when_userName_is_blank_throw_exception() throws Exception {

        assertThrows(Exception.class, () -> {
            salesforceService.getSalesforceToken(blankString, passwordValue);
        });
    }

    @Test
    public void getSalesforceToken_when_password_is_blank_throw_exception() throws Exception {

        assertThrows(Exception.class, () -> {
            salesforceService.getSalesforceToken(userNameValue, blankString);
        });
    }

    @Test
    public void getSalesforceToken_getToken_returns_null_throws_Exception() throws Exception {
        when(salesForceRestClient.getToken(any())).thenReturn(null);

        assertThrows(Exception.class, () -> {
            salesforceService.getSalesforceToken(userNameValue, passwordValue);
        });
    }

    @Test
    public void getSalesforceToken_when_Valid_details_given_returns_response() throws Exception {
        when(salesForceRestClient.getToken(any())).thenReturn(getAccessTokenResponse());

        String actualToken = salesforceService.getSalesforceToken(userNameValue, passwordValue);
        Assertions.assertEquals(actualToken,token);
    }



}
