package com.induction.sales.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    private String subject;
    private String startDateTime;
    private String endDateTime;
    private Integer durationInMinutes;
    private String activityDateTime;

}
