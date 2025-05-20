package com.example.weather.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DayForecast {
    private String day_name;
    private double temp_high_celsius;
    private String forecast_blurp;
}