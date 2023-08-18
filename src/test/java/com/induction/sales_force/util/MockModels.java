package com.induction.sales_force.util;

import com.induction.sales_force.dto.AccessTokenResponse;
import com.induction.sales_force.dto.Event;

import static com.induction.sales_force.util.TestCasesConstantApp.SUBJECT;
import static com.induction.sales_force.util.TestCasesConstantApp.START_DATE_TIME;
import static com.induction.sales_force.util.TestCasesConstantApp.END_DATE_TIME;
import static com.induction.sales_force.util.TestCasesConstantApp.ACTIVITY_DATE_TIME;
import static com.induction.sales_force.util.TestCasesConstantApp.DURATION_IN_MINUTES;
import static com.induction.sales_force.util.TestCasesConstantApp.TOKEN;

public class MockModels {

    public static Event getEvent() {
        return new Event(SUBJECT, START_DATE_TIME,
                END_DATE_TIME, DURATION_IN_MINUTES, ACTIVITY_DATE_TIME);
    }

    public static AccessTokenResponse getAccessTokenResponse() {
        return new AccessTokenResponse(TOKEN);
    }
}
