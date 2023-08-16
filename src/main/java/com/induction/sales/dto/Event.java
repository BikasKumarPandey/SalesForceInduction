package com.induction.sales.dto;

import lombok.*;
import org.springframework.stereotype.Service;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    private String Subject;
    private String StartDateTime;
    private String EndDateTime;
    private Integer DurationInMinutes;
    private String ActivityDateTime;


   /* public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public String getStartDateTime() {
        return StartDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        StartDateTime = startDateTime;
    }

    public String getEndDateTime() {
        return EndDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        EndDateTime = endDateTime;
    }

    public Integer getDurationInMinutes() {
        return DurationInMinutes;
    }

    public void setDurationInMinutes(Integer durationInMinutes) {
        DurationInMinutes = durationInMinutes;
    }

    public String getActivityDateTime() {
        return ActivityDateTime;
    }

    public void setActivityDateTime(String activityDateTime) {
        ActivityDateTime = activityDateTime;
    }*/
}
