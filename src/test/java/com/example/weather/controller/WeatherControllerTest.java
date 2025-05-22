package com.example.weather.controller;

import com.example.weather.model.DayForecast;
import com.example.weather.service.WeatherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.when;

@WebFluxTest(controllers = WeatherController.class)
public class WeatherControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private WeatherService weatherService;

    @Test
    void testForecastEndpointReturnsExpectedResponse() {
        DayForecast mockForecast = new DayForecast("Monday", 27.2, "Partly Sunny");

        when(weatherService.getTodayForecast()).thenReturn(Mono.just(mockForecast));

        webTestClient.get()
                .uri("/forecast")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.daily[0].day_name").isEqualTo("Monday")
                .jsonPath("$.daily[0].temp_high_celsius").isEqualTo(27.2)
                .jsonPath("$.daily[0].forecast_blurp").isEqualTo("Partly Sunny");
    }
}
