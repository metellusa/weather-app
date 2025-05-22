package com.example.weather.model;

import lombok.Data;
import java.util.List;

@Data
public class ForecastResponse {
    private Properties properties;

    @Data
    public static class Properties {
        private List<Period> periods;
    }

    @Data
    public static class Period {
        private String startTime;
        private int temperature;
        private String temperatureUnit;
        private String shortForecast;
    }
}