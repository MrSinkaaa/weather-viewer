package ru.mrsinkaaa.dto;


import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class WeatherDTO {

    private BigDecimal longitude;
    private BigDecimal latitude;

    private String city;

    private Double temperature;
    private Double feelsLike;
    private Double pressure;
    private Double humidity;
    private Double windSpeed;

    private WeatherCode weatherCode;

    private String sunrise;
    private String sunset;

}
