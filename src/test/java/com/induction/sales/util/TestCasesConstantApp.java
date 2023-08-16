package com.induction.sales.util;

public class TestCasesConstantApp {

    public static final String token = "RANDOM_TOKEN";
    public static final String getSalesForceTokenUrl = "/api/salesforce/tokenGenerator";
    public static final String eventInSalesForceUrl = "/api/salesforce/event";
    public static final String getEventFromSalesForceTest = "/api/salesforce/event";


    public static final String userNameKey = "userName";
    public static final String userNameValue = "RANDOM_USER_NAME";
    public static final String passwordKey = "password";
    public static final String passwordValue = "RANDOM_PASSWORD";
    public static final String authorizationKey = "Authorization";
    public static final String authorizationValue = "Bearer mockToken";






    public final static String salesForceTokenUrl = "https://login.salesforce.com/services/oauth2/token";
    public final static String createSalesForceEventBaseURl = "https://sacumen7-dev-ed.develop.my.salesforce.com";
    public final static String createSalesForceEventURl = createSalesForceEventBaseURl + "/services/data/v53.0/sobjects/Event/";
    public final static String getSalesForceEventUrl = createSalesForceEventBaseURl + "/services/data/v58.0/query?q=SELECT Id, Subject, StartDateTime, EndDateTime FROM Event";

}
