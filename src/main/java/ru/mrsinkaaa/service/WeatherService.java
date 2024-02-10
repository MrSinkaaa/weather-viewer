package ru.mrsinkaaa.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ru.mrsinkaaa.api.WeatherAPI;
import ru.mrsinkaaa.config.AppConfig;
import ru.mrsinkaaa.dto.WeatherCode;
import ru.mrsinkaaa.dto.WeatherDTO;
import ru.mrsinkaaa.exceptions.api.APIResponseException;
import ru.mrsinkaaa.exceptions.location.LocationNotFoundException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WeatherService {

    private static final WeatherService INSTANCE = new WeatherService();
    private final WeatherAPI weatherAPI = WeatherAPI.getInstance();

    public WeatherDTO getWeather(String city) throws APIResponseException, LocationNotFoundException {
        String url = AppConfig.getProperty("api.url.city")
                .formatted(city, AppConfig.getProperty("api.key"));

        return processWeatherApiResponse(url);
    }

    @SneakyThrows
    public WeatherDTO getWeather(BigDecimal latitude, BigDecimal longitude) {
        String url = AppConfig.getProperty("api.url.location")
                .formatted(latitude, longitude, AppConfig.getProperty("api.key"));

        return processWeatherApiResponse(url);
    }

    private WeatherDTO processWeatherApiResponse(String url) {
        try {
            String resp = weatherAPI.sendGetRequest(url);
            log.debug("Response from weatherAPI: {}", resp);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonWeather = mapper.readTree(resp);

            return parseJsonToDTO(jsonWeather);
        } catch (FileNotFoundException e) {
            log.error(e.getMessage(), e);
            throw new LocationNotFoundException();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new APIResponseException();
        }
    }

    private static WeatherDTO parseJsonToDTO(JsonNode jsonWeather) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.convertValue(jsonWeather, WeatherDTO.class);
    }


    private static String parseUTCtoLocalDateTime(Long unixTimestamp, int timezone) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        Instant instant = Instant.ofEpochSecond(unixTimestamp);
        ZoneOffset zoneOffset = ZoneOffset.ofTotalSeconds(timezone);

        return LocalDateTime.ofInstant(instant, zoneOffset).format(formatter);
    }

    public static WeatherService getInstance() {
        return INSTANCE;
    }
}
