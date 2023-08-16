package com.induction.sales.util;

import com.induction.sales.dto.AccessTokenResponse;
import com.induction.sales.dto.Event;

import static com.induction.sales.util.TestCasesConstantApp.SUBJECT;
import static com.induction.sales.util.TestCasesConstantApp.START_DATE_TIME;
import static com.induction.sales.util.TestCasesConstantApp.END_DATE_TIME;
import static com.induction.sales.util.TestCasesConstantApp.ACTIVITY_DATE_TIME;
import static com.induction.sales.util.TestCasesConstantApp.DURATION_IN_MINUTES;
import static com.induction.sales.util.TestCasesConstantApp.TOKEN;

public class MockModels {

    public static Event getEvent() {
        return new Event(SUBJECT, START_DATE_TIME,
                END_DATE_TIME, DURATION_IN_MINUTES, ACTIVITY_DATE_TIME);
    }

    public static AccessTokenResponse getAccessTokenResponse() {
        return new AccessTokenResponse(TOKEN);
    }
}
