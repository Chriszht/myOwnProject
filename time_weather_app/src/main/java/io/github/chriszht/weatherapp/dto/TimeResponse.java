package io.github.chriszht.weatherapp.dto;

import lombok.Data;

@Data
public class TimeResponse {
    private String timezone;
    private String datetime;
    private String dayOfWeek;
    private String abbreviation;
}