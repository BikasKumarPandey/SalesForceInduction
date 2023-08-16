package com.induction.sales.util;

import com.induction.sales.dto.Event;

public class MockModels {

    public static Event getEvent() {
        return new Event("RANDOM SUBJECT", "2023-08-15T10:00:00Z", 
                "2023-08-15T12:00:00Z", 120, "2023-08-15T10:00:00Z");
    }
}
