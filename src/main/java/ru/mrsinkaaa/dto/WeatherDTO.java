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
    private String country;

    private String weatherIcon;

    private String sunrise;
    private String sunset;

    private Double temperature;
    private Double humidity;
    private Double pressure;
    private Double visibility;

    private Double windSpeed;
    private Double windDeg;
}
