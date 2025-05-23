package com.example.weather.controller;

import com.example.weather.model.DayForecast;
import com.example.weather.model.ForecastResponse;
import com.example.weather.service.WeatherService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class WeatherController {

    // Inject the WeatherService
    private final WeatherService weatherService;

    /**
     * Exposes a simplified endpoint that returns the current day's weather forecast.
     * - Response structure matches the required format: { "daily": [ ... ] }
     *
     * Example response:
     * {
     *   "daily": [
     *     {
     *       "day_name": "Monday",
     *       "temp_high_celsius": 27.2,
     *       "forecast_blurp": "Partly Sunny"
     *     }
     *   ]
     * }
     */
    @GetMapping("/forecast")
    public Mono<Map<String, List<DayForecast>>> getTodayForecast() {
        return weatherService.getTodayForecast()
                .map(forecast -> Map.of("daily", List.of(forecast)));
    }

    /**
     * Exposes a raw version of the NOAA API response for debugging.
     * - No transformation applied
     * - Returns full JSON mapped to ForecastResponse structure
     */
    @GetMapping("/raw-forecast")
    public Mono<ForecastResponse> getRawForecast() {
        return weatherService.fetchRawForecast();
    }
}
