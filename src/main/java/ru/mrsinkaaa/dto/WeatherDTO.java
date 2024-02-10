package ru.mrsinkaaa.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Data
@NoArgsConstructor
public class WeatherDTO {

    private BigDecimal longitude;
    private BigDecimal latitude;

    private String city;

    private Integer temperature;
    private Integer feelsLike;
    private Double pressure;
    private Double humidity;
    private Double windSpeed;

    private WeatherCode weatherCode;

    private String sunrise;
    private String sunset;

    private int timeZone;

    @JsonProperty("main")
    private void unpackMain(JsonNode main) {
        this.temperature = main.get("temp").asInt();
        this.feelsLike = main.get("feels_like").asInt();
        this.pressure = main.get("pressure").asDouble();
        this.humidity = main.get("humidity").asDouble();
    }

    @JsonProperty("name")
    private void unpackName(JsonNode name) {
        this.city = name.asText();
    }

    @JsonProperty("coord")
    private void unpackCoord(JsonNode coord) {
        this.longitude = coord.get("lon").decimalValue();
        this.latitude = coord.get("lat").decimalValue();
    }

    @JsonProperty("weather")
    private void unpackWeather(JsonNode weather) {
        this.weatherCode = getIcon(weather.get(0).get("main").asText());
    }

    @JsonProperty("wind")
    private void unpackWind(JsonNode wind) {
        this.windSpeed = wind.get("speed").asDouble();
    }

    @JsonProperty("sys")
    private void unpackSys(JsonNode sys) {
        this.sunrise = parseUTCtoLocalDateTime(sys.get("sunrise").asLong(), timeZone);
        this.sunset = parseUTCtoLocalDateTime(sys.get("sunset").asLong(), timeZone);
    }

    @JsonProperty("timezone")
    private void unpackTimeZone(JsonNode timezone) {
        this.timeZone = timezone.asInt();
    }

    private static String parseUTCtoLocalDateTime(Long unixTimestamp, int timezone) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        Instant instant = Instant.ofEpochSecond(unixTimestamp);
        ZoneOffset zoneOffset = ZoneOffset.ofTotalSeconds(timezone);

        return LocalDateTime.ofInstant(instant, zoneOffset).format(formatter);
    }

    private static WeatherCode getIcon(String value) {
        return WeatherCode.valueOf(value.toUpperCase());
    }

}
