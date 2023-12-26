package ru.mrsinkaaa.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import ru.mrsinkaaa.api.WeatherAPI;
import ru.mrsinkaaa.config.AppConfig;
import ru.mrsinkaaa.dto.WeatherCode;
import ru.mrsinkaaa.dto.WeatherDTO;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WeatherService {

    private static final WeatherService INSTANCE = new WeatherService();
    private final WeatherAPI weatherAPI = WeatherAPI.getInstance();

    @SneakyThrows
    public WeatherDTO getWeather(String city) {
        String url = AppConfig.getProperty("api.url.city").formatted(city, AppConfig.getProperty("api.key"));

        String resp = weatherAPI.sendGetRequest(url);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonWeather = mapper.readTree(resp);

        return parseJsonToDTO(jsonWeather);
    }

    public WeatherDTO getWeather(BigDecimal latitude, BigDecimal longitude) throws IOException {
        String url = AppConfig.getProperty("api.url.location").formatted(latitude, longitude, AppConfig.getProperty("api.key"));

        String resp = weatherAPI.sendGetRequest(url);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonWeather = mapper.readTree(resp);

        return parseJsonToDTO(jsonWeather);
    }


    private static WeatherDTO parseJsonToDTO(JsonNode jsonWeather) {
        return WeatherDTO.builder()
                .longitude(BigDecimal.valueOf(jsonWeather.get("coord").get("lon").asDouble()))
                .latitude(BigDecimal.valueOf(jsonWeather.get("coord").get("lat").asDouble()))
                .city(jsonWeather.get("name").asText())
                .weatherCode(getIcon(jsonWeather.get("weather").get(0).get("main").asText()))
                .sunrise(parseUTCtoLocalDateTime(jsonWeather.get("sys").get("sunrise").asLong()))
                .sunset(parseUTCtoLocalDateTime(jsonWeather.get("sys").get("sunset").asLong()))
                .temperature(convertTempToCelsius(jsonWeather.get("main").get("temp").asInt()))
                .feelsLike(convertTempToCelsius(jsonWeather.get("main").get("feels_like").asInt()))
                .humidity(Double.valueOf(jsonWeather.get("main").get("humidity").asText()))
                .pressure(Double.valueOf(jsonWeather.get("main").get("pressure").asText()))
                .windSpeed(Double.valueOf(jsonWeather.get("wind").get("speed").asText()))
                .build();
    }

    private static WeatherCode getIcon(String value) {
        return WeatherCode.valueOf(value.toUpperCase());
    }

    private static int convertTempToCelsius(int temp) {
        return (int) Math.abs(Math.ceil(temp - 273.15));
    }

    private static String parseUTCtoLocalDateTime(Long unixTimestamp) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy");
        Instant instant = Instant.ofEpochSecond(unixTimestamp);

        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).format(formatter);
    }

    public static WeatherService getInstance() {
        return INSTANCE;
    }
}
