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

import static com.induction.sales.util.MockModels.getAccessTokenResponse;
import static com.induction.sales.util.TestCasesConstantApp.USER_NAME_VALUE;
import static com.induction.sales.util.TestCasesConstantApp.PASSWORD_VALUE;
import static com.induction.sales.util.TestCasesConstantApp.BLANK;
import static com.induction.sales.util.TestCasesConstantApp.TOKEN;
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
    @DisplayName("JUnit test for getAllEmployees method (negative scenario)")
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
    public void getSalesforceToken_when_Valid_details_given_returns_response() throws Exception {
        when(salesForceRestClient.getToken(any())).thenReturn(getAccessTokenResponse());

        String actualToken = salesforceService.getSalesforceToken(USER_NAME_VALUE, PASSWORD_VALUE);
        Assertions.assertEquals(actualToken,TOKEN);
    }



}
