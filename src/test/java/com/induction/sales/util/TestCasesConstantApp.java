package com.induction.sales.util;

public class TestCasesConstantApp {

    public static final String TOKEN = "Random_token";
    public static final String GET_SALESFORCE_TOKEN_URL = "/api/salesforce/tokenGenerator";
    public static final String SALESFORCE_EVENT_URL = "/api/salesforce/event";


    public static final String USER_NAME_KEY = "userName";
    public static final String USER_NAME_VALUE = "Random user neme";
    public static final String BLANK ="   \t\n";;
    public static final String PASSWORD_KEY = "password";
    public static final String PASSWORD_VALUE = "RandomPassword";
    public static final String AUTHORIZATION_KEY = "Authorization";
    public static final String AUTHORIZATION_VALUE = "Bearer mockToken";
    public static final String SUBJECT = "Random subject";
    public static final String START_DATE_TIME = "2023-08-15T10:00:00Z";
    public static final String END_DATE_TIME = "2023-08-15T10:00:00Z";
    public static final String ACTIVITY_DATE_TIME = "2023-08-15T10:00:00Z";
    public static final int DURATION_IN_MINUTES = 120;






    public final static String salesForceTokenUrl = "https://login.salesforce.com/services/oauth2/token";
    public final static String createSalesForceEventBaseURl = "https://sacumen7-dev-ed.develop.my.salesforce.com";
    public final static String CREATE_SALESFORCE_EVENT_URL = createSalesForceEventBaseURl + "/services/data/v53.0/sobjects/Event/";
    public final static String getSalesForceEventUrl = createSalesForceEventBaseURl + "/services/data/v58.0/query?q=SELECT Id, Subject, StartDateTime, EndDateTime FROM Event";

}
