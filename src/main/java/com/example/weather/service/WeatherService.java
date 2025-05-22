package com.example.weather.service;

import lombok.RequiredArgsConstructor;

import java.time.ZonedDateTime;
import java.time.format.TextStyle;
import java.util.Locale;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.weather.model.DayForecast;
import com.example.weather.model.ForecastResponse;

import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class WeatherService {

    // Injected reactive HTTP client
    private final WebClient webClient;

    /**
     * Makes a non-blocking HTTP GET request to the NOAA weather API and 
     * deserializes the JSON response into a ForecastResponse object.
     */
    public Mono<ForecastResponse> fetchRawForecast() {
        return webClient
                .get()
                .uri("https://api.weather.gov/gridpoints/MLB/33,70/forecast")
                .retrieve()
                .bodyToMono(ForecastResponse.class);
    }

    /**
     * Extracts the current day's forecast from the raw NOAA response,
     * transforms it into a simplified structure, and returns it as a Mono.
     */
    public Mono<DayForecast> getTodayForecast() {
        return fetchRawForecast()
                .flatMap(response -> {
                    // Grab the list of forecast periods from the response
                    var periods = response.getProperties().getPeriods();

                    // If periods is null or empty, return an empty result
                    if (periods == null || periods.isEmpty()) {
                        return Mono.empty();
                    }

                    // NOAA’s API returns a list ordered by time — so the first entry is “now”
                    var today = periods.get(0);

                    // Convert temperature from Fahrenheit to Celsius, rounded to 1 decimal place
                    double celsius = (today.getTemperature() - 32) * 5.0 / 9.0;

                    // The `name` field may say "Tonight" or "This Afternoon", so we parse `startTime` instead
                    ZonedDateTime zonedDateTime = ZonedDateTime.parse(today.getStartTime());
                    String dayName = zonedDateTime.getDayOfWeek()
                            .getDisplayName(TextStyle.FULL, Locale.ENGLISH);

                    // Create a simplified forecast DTO with transformed values
                    DayForecast result = new DayForecast(
                            dayName,
                            Math.round(celsius * 10.0) / 10.0,
                            today.getShortForecast()
                    );

                    return Mono.just(result);
                });
    }
}
