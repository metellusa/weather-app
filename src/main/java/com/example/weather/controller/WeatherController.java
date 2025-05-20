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

    private final WeatherService weatherService;

    @GetMapping("/forecast")
    public Mono<Map<String, List<DayForecast>>> getTodayForecast() {
        return weatherService.getTodayForecast()
                .map(forecast -> Map.of("daily", List.of(forecast)));
    }

    @GetMapping("/raw-forecast")
    public Mono<ForecastResponse> getRawForecast() {
        return weatherService.fetchRawForecast();
    }
}