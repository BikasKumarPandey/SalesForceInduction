package com.induction.sales.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.induction.sales.api.SalesForceController;
import com.induction.sales.dto.Event;
import com.induction.sales.service.SalesforceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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

import static com.induction.sales.util.TestCasesConstantApp.getSalesForceTokenUrl;
import static com.induction.sales.util.TestCasesConstantApp.eventInSalesForceUrl;
import static com.induction.sales.util.TestCasesConstantApp.authorizationKey;
import static com.induction.sales.util.TestCasesConstantApp.authorizationValue;
import static com.induction.sales.util.TestCasesConstantApp.userNameKey;
import static com.induction.sales.util.TestCasesConstantApp.userNameValue;
import static com.induction.sales.util.TestCasesConstantApp.token;
import static com.induction.sales.util.TestCasesConstantApp.passwordKey;
import static com.induction.sales.util.TestCasesConstantApp.passwordValue;

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

        when(salesforceService.getSalesforceToken(anyString(), anyString())).thenReturn(token);

        mockMvc.perform(get(getSalesForceTokenUrl)
                        .param(userNameKey, userNameValue)
                        .param(passwordKey, passwordValue)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(token));
    }


    // TODO: 16/08/23 Remove hard coded value
    @Test
    public void createEventInSalesForceTest() throws Exception {
        Event event = new Event();
        when(salesforceService.createEventInSalesForce(any(Event.class), anyString()))
                .thenReturn(ResponseEntity.ok(new ObjectMapper().writeValueAsString(getEvent())));

        mockMvc.perform(post(eventInSalesForceUrl)
                        .header(authorizationKey, authorizationValue)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(getEvent()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        event.setSubject("Important Meeting");
        event.setStartDateTime("2023-08-15T10:00:00Z");
        event.setEndDateTime("2023-08-15T12:00:00Z");
        event.setDurationInMinutes(120); // Set the duration in minutes
        event.setActivityDateTime("2023-08-15T10:00:00Z"); // Set the activity date and time

        verify(salesforceService).createEventInSalesForce(event, authorizationValue);
    }


    @Test
    public void getEventFromSalesForceTest() throws Exception {
        when(salesforceService.getEventFromSalesForce(anyString())).thenReturn(ResponseEntity.ok(token));

        mockMvc.perform(get(eventInSalesForceUrl)
                        .header(authorizationKey, authorizationValue)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(token));
    }

}
