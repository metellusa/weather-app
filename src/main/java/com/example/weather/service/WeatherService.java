package com.example.weather.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.weather.model.DayForecast;
import com.example.weather.model.ForecastResponse;

import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class WeatherService {

    private final WebClient webClient;

    public Mono<ForecastResponse> fetchRawForecast() {
        return webClient
                .get()
                .uri("https://api.weather.gov/gridpoints/MLB/33,70/forecast")
                .retrieve()
                .bodyToMono(ForecastResponse.class);
    }

    public Mono<DayForecast> getTodayForecast() {
        return fetchRawForecast()
                .flatMap(response -> {
                    var periods = response.getProperties().getPeriods();
                    if (periods == null || periods.isEmpty()) {
                        return Mono.empty();
                    }

                    var today = periods.get(0);
                    double celsius = (today.getTemperature() - 32) * 5.0 / 9.0;

                    DayForecast result = new DayForecast(
                            today.getName(),
                            Math.round(celsius * 10.0) / 10.0,
                            today.getShortForecast());

                    return Mono.just(result);
                });
    }
}