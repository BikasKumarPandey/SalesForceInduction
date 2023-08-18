package com.induction.sales_force.controller;


import com.induction.sales_force.api.SalesForceController;
import org.junit.jupiter.api.extension.ExtendWith;
import com.induction.sales_force.service.SalesforceService;
import com.induction.sales_force.dto.Event;
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
import static com.induction.sales_force.util.MockModels.getEvent;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import static com.induction.sales_force.util.TestCasesConstantApp.GET_SALESFORCE_TOKEN_URL;
import static com.induction.sales_force.util.TestCasesConstantApp.SALESFORCE_EVENT_URL;
import static com.induction.sales_force.util.TestCasesConstantApp.AUTHORIZATION_KEY;
import static com.induction.sales_force.util.TestCasesConstantApp.AUTHORIZATION_VALUE;
import static com.induction.sales_force.util.TestCasesConstantApp.USER_NAME_KEY;
import static com.induction.sales_force.util.TestCasesConstantApp.USER_NAME_VALUE;
import static com.induction.sales_force.util.TestCasesConstantApp.TOKEN;
import static com.induction.sales_force.util.TestCasesConstantApp.PASSWORD_KEY;
import static com.induction.sales_force.util.TestCasesConstantApp.PASSWORD_VALUE;

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
    public void getSalesforceTokenTest() throws Exception {
        when(salesforceService.getSalesforceToken(anyString(), anyString())).thenReturn(TOKEN);

        mockMvc.perform(get(GET_SALESFORCE_TOKEN_URL)
                        .param(USER_NAME_KEY, USER_NAME_VALUE)
                        .param(PASSWORD_KEY, PASSWORD_VALUE)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(TOKEN));
    }

    @Test
    public void createEventInSalesForceTest() throws Exception {
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
    public void getEventFromSalesForceTest() throws Exception {
        when(salesforceService.getEventFromSalesForce(anyString())).thenReturn(ResponseEntity.ok(TOKEN));

        mockMvc.perform(get(SALESFORCE_EVENT_URL)
                        .header(AUTHORIZATION_KEY, AUTHORIZATION_VALUE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(TOKEN));
    }

}
