package com.induction.sales.service;


import com.induction.sales.serviceImpl.SalesForceMsCommunication.SalesForceRestClient;
import com.induction.sales.serviceImpl.SalesforceServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static com.induction.sales.util.MockModels.getAccessTokenResponse;
import static com.induction.sales.util.MockModels.getEvent;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import static com.induction.sales.util.TestCasesConstantApp.PASSWORD_VALUE;
import static com.induction.sales.util.TestCasesConstantApp.USER_NAME_VALUE;
import static com.induction.sales.util.TestCasesConstantApp.BLANK;
import static com.induction.sales.util.TestCasesConstantApp.TOKEN;
import static com.induction.sales.util.TestCasesConstantApp.AUTHORIZATION_VALUE;

@ExtendWith(MockitoExtension.class)
public class SalesforceServiceImplTest {

    @InjectMocks
    private SalesforceServiceImpl salesforceService;

    @Mock
    private SalesForceRestClient salesForceRestClient;

    @Test
    @DisplayName("Get salesForceToken method negative test cases")
    public void getSalesforceToken_when_userName_is_empty_throw_exception() throws Exception {

        assertThrows(Exception.class, () -> {
            salesforceService.getSalesforceToken("", PASSWORD_VALUE);
        });
    }

    @Test
    public void getSalesforceToken_when_password_is_empty_throw_exception() throws Exception {

        assertThrows(Exception.class, () -> {
            salesforceService.getSalesforceToken(USER_NAME_VALUE, "");
        });
    }

    @Test
    public void getSalesforceToken_when_userName_is_blank_throw_exception() throws Exception {

        assertThrows(Exception.class, () -> {
            salesforceService.getSalesforceToken(BLANK, PASSWORD_VALUE);
        });
    }

    @Test
    public void getSalesforceToken_when_password_is_blank_throw_exception() throws Exception {

        assertThrows(Exception.class, () -> {
            salesforceService.getSalesforceToken(USER_NAME_VALUE, BLANK);
        });
    }

    @Test
    public void getSalesforceToken_getToken_returns_null_throws_Exception() throws Exception {
        when(salesForceRestClient.getToken(any())).thenReturn(null);

        assertThrows(Exception.class, () -> {
            salesforceService.getSalesforceToken(USER_NAME_VALUE, PASSWORD_VALUE);
        });
    }

    @Test
    @DisplayName("Get salesForceToken method positive test cases")
    public void getSalesforceToken_when_Valid_details_given_returns_response() throws Exception {
        when(salesForceRestClient.getToken(any())).thenReturn(getAccessTokenResponse());

        String actualToken = salesforceService.getSalesforceToken(USER_NAME_VALUE, PASSWORD_VALUE);
        Assertions.assertEquals(TOKEN, actualToken);
    }

    @Test
    @DisplayName("Get reateEventInSalesForce method negative test cases")
    public void createEventInSalesForce_when_given_request_is_null_throws_expection() {

        assertThrows(Exception.class, () -> {
            salesforceService.createEventInSalesForce(null, AUTHORIZATION_VALUE);
        });

    }

    @Test
    public void createEventInSalesForce_when_restclass_gives_null_throws_expection() throws Exception {
        when(salesForceRestClient.createEventInSalesForce(anyString(), any())).thenReturn(null);

        assertThrows(Exception.class, () -> {
            salesforceService.createEventInSalesForce(getEvent(), AUTHORIZATION_VALUE);
        });
    }

    @Test
    @DisplayName("Get reateEventInSalesForce method positive test cases")
    public void createEventInSalesForce_when_restclass_gives_notNull_return_success() throws Exception {
        when(salesForceRestClient.createEventInSalesForce(anyString(), any())).thenReturn(ResponseEntity.ok(TOKEN));

        ResponseEntity<String> actualValue = salesforceService.createEventInSalesForce(getEvent(), AUTHORIZATION_VALUE);
        Assertions.assertEquals(TOKEN, actualValue.getBody());
    }

    @Test
    public void getEventFromSalesForce_when_given_invalid_token_throws_exception() throws Exception {

        assertThrows(Exception.class, () -> {
            salesforceService.getEventFromSalesForce(null);
        });
    }

    @Test
    public void getEventFromSalesForce_when_restClass_returns_null_throws_exception() throws Exception {
        when(salesForceRestClient.getEventFromSalesForce(anyString(), any())).thenReturn(null);

        assertThrows(Exception.class, () -> {
            salesforceService.getEventFromSalesForce(AUTHORIZATION_VALUE);
        });
    }

    @Test
    public void getEventFromSalesForce_when_restClass_returns_notNull_returns_response() throws Exception {
        when(salesForceRestClient.getEventFromSalesForce(anyString(), any())).thenReturn(ResponseEntity.ok(getEvent().toString()));

        ResponseEntity<String> eventFromSalesForce = salesforceService.getEventFromSalesForce(AUTHORIZATION_VALUE);
        String actualEvent = eventFromSalesForce.getBody();

        Assertions.assertEquals(getEvent().toString(), actualEvent);
    }

}
