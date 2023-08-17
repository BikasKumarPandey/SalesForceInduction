package com.induction.sales.controller;


import com.induction.sales.api.SalesForceController;
import org.junit.jupiter.api.extension.ExtendWith;
import com.induction.sales.service.SalesforceService;
import com.induction.sales.dto.Event;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.induction.sales.util.MockModels.getEvent;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import static com.induction.sales.util.TestCasesConstantApp.GET_SALESFORCE_TOKEN_URL;
import static com.induction.sales.util.TestCasesConstantApp.SALESFORCE_EVENT_URL;
import static com.induction.sales.util.TestCasesConstantApp.AUTHORIZATION_KEY;
import static com.induction.sales.util.TestCasesConstantApp.AUTHORIZATION_VALUE;
import static com.induction.sales.util.TestCasesConstantApp.USER_NAME_KEY;
import static com.induction.sales.util.TestCasesConstantApp.USER_NAME_VALUE;
import static com.induction.sales.util.TestCasesConstantApp.TOKEN;
import static com.induction.sales.util.TestCasesConstantApp.PASSWORD_KEY;
import static com.induction.sales.util.TestCasesConstantApp.PASSWORD_VALUE;

@ExtendWith(MockitoExtension.class)
public class SalesForceControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private SalesForceController salesForceController;

    @Mock
    private SalesforceService salesforceService;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(salesForceController).build();
    }

    @Test
    public void getSalesforceToken_when_valid_param_given_gives_response() throws Exception {
        when(salesforceService.getSalesforceToken(anyString(), anyString())).thenReturn(TOKEN);

        mockMvc.perform(get(GET_SALESFORCE_TOKEN_URL)
                        .param(USER_NAME_KEY, USER_NAME_VALUE)
                        .param(PASSWORD_KEY, PASSWORD_VALUE)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(TOKEN));
    }

    @Test
    public void getSalesforceToken_when_Params_missing_throws_exception() throws Exception {

        mockMvc.perform(get(GET_SALESFORCE_TOKEN_URL)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getSalesforceToken_Incorrect_Url_Throws_NotFoundException() throws Exception {

        mockMvc.perform(get("/RANDOM_URL")
                        .param(USER_NAME_KEY, USER_NAME_VALUE)
                        .param(PASSWORD_KEY, PASSWORD_VALUE)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void createEventInSalesForce_when_given_valid_params_gives_response() throws Exception {
        when(salesforceService.createEventInSalesForce(any(Event.class), anyString()))
                .thenReturn(ResponseEntity.ok(new ObjectMapper().writeValueAsString(getEvent())));

        mockMvc.perform(post(SALESFORCE_EVENT_URL)
                        .header(AUTHORIZATION_KEY, AUTHORIZATION_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(getEvent()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(salesforceService).createEventInSalesForce(getEvent(), AUTHORIZATION_VALUE);
    }

    @Test
    public void createEventInSalesForce_when_event_not_given_throws_error() throws Exception {

        mockMvc.perform(post(SALESFORCE_EVENT_URL)
                        .header("Authorization", "valid-token")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createEventInSalesForce_when_authorization_not_given_throws_error() throws Exception {

        mockMvc.perform(post(SALESFORCE_EVENT_URL)
                        .content(getEvent().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createEventInSalesForce_Incorrect_Url_Throws_NotFoundException() throws Exception {

        mockMvc.perform(get("/RANDOM_URL")
                        .content(getEvent().toString()) // Replace with your actual JSON payload
                        .header(AUTHORIZATION_KEY, AUTHORIZATION_VALUE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    @Test
    public void getEventFromSalesForce_when_valid_header_provided_gives_response() throws Exception {
        when(salesforceService.getEventFromSalesForce(anyString())).thenReturn(ResponseEntity.ok(TOKEN));

        mockMvc.perform(get(SALESFORCE_EVENT_URL)
                        .header(AUTHORIZATION_KEY, AUTHORIZATION_VALUE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(TOKEN));
    }

    @Test
    public void getEventFromSalesForce_when_header_notProvided_gives_response() throws Exception {

        mockMvc.perform(get(SALESFORCE_EVENT_URL)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getEventFromSalesForce_Incorrect_Url_Throws_NotFoundException() throws Exception {

        mockMvc.perform(get("/RANDOM_URL")
                        .header(AUTHORIZATION_KEY, AUTHORIZATION_VALUE))
                .andExpect(status().isNotFound());
    }


}
