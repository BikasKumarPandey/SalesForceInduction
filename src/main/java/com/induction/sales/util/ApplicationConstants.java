package com.induction.sales.util;

public class ApplicationConstants {

    public final static String salesForceTokenUrl = "https://login.salesforce.com/services/oauth2/token";
    public final static String createSalesForceEventBaseURl = "https://sacumen7-dev-ed.develop.my.salesforce.com";
    public final static String createSalesForceEventURl = createSalesForceEventBaseURl + "/services/data/v53.0/sobjects/Event/";
    public final static String grantType = "grant_type=password";
    public final static String clientId = "&client_id";
    public final static String clientSecret = "&client_secret";
    public final static String userName = "&username";
    public final static String password = "&password";
    public final static String authorizationToken = "&Authorization";
}
