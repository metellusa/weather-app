package com.example.weather.service;

import com.example.weather.model.DayForecast;
import com.example.weather.model.ForecastResponse;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class WeatherServiceTest {

    @SuppressWarnings("unchecked")
    @Test
    void testGetTodayForecastTransformsCorrectly() {
        // Setup mock WebClient
        ForecastResponse.Period mockPeriod = new ForecastResponse.Period();
        mockPeriod.setStartTime("2025-05-21T13:00:00-04:00");
        mockPeriod.setTemperature(81);
        mockPeriod.setTemperatureUnit("F");
        mockPeriod.setShortForecast("Partly Sunny");

        ForecastResponse.Properties props = new ForecastResponse.Properties();
        props.setPeriods(List.of(mockPeriod));

        ForecastResponse forecast = new ForecastResponse();
        forecast.setProperties(props);

        WebClient webClient = mock(WebClient.class);
        @SuppressWarnings("rawtypes")
        WebClient.RequestHeadersUriSpec request = mock(WebClient.RequestHeadersUriSpec.class);
        @SuppressWarnings("rawtypes")
        WebClient.RequestHeadersSpec headersSpec = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);

        when(webClient.get()).thenReturn(request);
        when(request.uri(any(String.class))).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(ForecastResponse.class)).thenReturn(Mono.just(forecast));

        WeatherService weatherService = new WeatherService(webClient);

        Mono<DayForecast> result = weatherService.getTodayForecast();

        StepVerifier.create(result)
                .expectNextMatches(df -> 
                        df.getDay_name().equals("Wednesday") &&
                        Math.abs(df.getTemp_high_celsius() - 27.2) < 0.1 &&
                        df.getForecast_blurp().equals("Partly Sunny")
                )
                .verifyComplete();
    }
}
