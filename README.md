# 🌤️ Reactive Weather Forecast API

This project is a take-home coding exercise for Disney's technical interview process. It demonstrates a reactive microservice built with Spring Boot WebFlux that consumes a public weather API and returns simplified forecast data for the current day.

---

## 📌 Overview

This application exposes a single REST endpoint that:

- Makes a **non-blocking** call to [NOAA's weather API](https://api.weather.gov/gridpoints/MLB/33,70/forecast)
- Extracts the current day's high temperature and weather summary
- Converts the temperature to Celsius
- Returns a simplified JSON response using **Spring WebFlux**

---

## 📫 Endpoint

- **GET** `/forecast`

### ✅ Example Response

```json
{
  "daily": [
    {
      "day_name": "Monday",
      "temp_high_celsius": 27.2,
      "forecast_blurp": "Partly Sunny"
    }
  ]
}
