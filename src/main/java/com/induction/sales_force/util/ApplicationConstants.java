package com.induction.sales_force.util;

public class ApplicationConstants {
    private ApplicationConstants() {
    }

    public static final String SALES_FORCE_TOKEN_URL = "https://login.salesforce.com/services/oauth2/tokens";
    public static final String SALES_FORCE_EVENT_BASE_URL = "https://sacumen7-dev-ed.develop.my.salesforce.com";
    public static final String SALES_FORCE_CREATE_EVENT_URL = SALES_FORCE_EVENT_BASE_URL + "/services/data/v53.0/sobjects/Event/";
    public static final String SALES_FORCE_GET_EVENT_URL = SALES_FORCE_EVENT_BASE_URL + "/services/data/v58.0/query?q=SELECT Id, Subject, StartDateTime, EndDateTime FROM Event";


    public static final String AUTHORIZATION_KEY = "Authorization";
    public static final String BEARER = "Bearer ";
    public static final String GRANT_TYPE = "grant_type=password";
    public static final String CLIENT_ID = "&client_id";
    public static final String CLIENT_SECRET = "&client_secret";
    public static final String USER_NAME_KEY = "&username";
    public static final String PASSWORD_KEY = "&password";

}
