package com.induction.sales.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventResponse {
    private String id;
    private String subject;
    private String startDateTime;
    private String endDateTime;
}
