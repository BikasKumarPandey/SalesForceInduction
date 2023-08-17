package com.induction.sales.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
// TODO: 17/08/23 check this is unused
public class EventResponse {
    private String id;
    private String subject;
    private String startDateTime;
    private String endDateTime;
}
