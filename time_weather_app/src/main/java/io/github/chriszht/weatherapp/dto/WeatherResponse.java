package io.github.chriszht.weatherapp.dto;

import lombok.Data;

@Data
public class WeatherResponse {
    private String location;
    private double temperature;
    private double feelsLike;
    private int humidity;
    private String description;
    private String icon;
    private double windSpeed;
}