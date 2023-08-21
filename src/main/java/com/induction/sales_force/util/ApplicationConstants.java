package com.induction.sales_force.util;

public class ApplicationConstants {
    private ApplicationConstants() {
    }

    public static final String SALES_FORCE_TOKEN_URL = "https://login.salesforce.com/services/oauth2/token";
    public static final String SALES_FORCE_EVENT_BASE_URL = "https://sacumen7-dev-ed.develop.my.salesforce.com";
    public static final String PATH_SEGMENT = "/services/data/v58.0/";
    public static final String SALES_FORCE_CREATE_EVENT_URL = SALES_FORCE_EVENT_BASE_URL + PATH_SEGMENT + "sobjects/Event/";
    public static final String QUERY = "SELECT Id, Subject, StartDateTime, EndDateTime FROM Event";
    public static final String SALES_FORCE_GET_EVENT_URL = SALES_FORCE_EVENT_BASE_URL + PATH_SEGMENT + "/query?q=" + QUERY;


    public static final String AUTHORIZATION_KEY = "Authorization";
    public static final String BEARER = "Bearer ";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String CONTENT_TYPE_JSON = "application/json";
    public static final String GRANT_TYPE = "grant_type=password";
    public static final String CLIENT_ID = "&client_id";
    public static final String CLIENT_SECRET = "&client_secret";
    public static final String USER_NAME_KEY = "&username";
    public static final String PASSWORD_KEY = "&password";

}
