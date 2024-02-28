package ru.mrsinkaaa.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ru.mrsinkaaa.api.WeatherAPI;
import ru.mrsinkaaa.config.AppConfig;
import ru.mrsinkaaa.dto.WeatherDTO;
import ru.mrsinkaaa.exception.api.APIResponseException;
import ru.mrsinkaaa.exception.location.LocationNotFoundException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Slf4j
public class WeatherService {

    private final WeatherAPI weatherAPI;

    public WeatherService(WeatherAPI weatherAPI) {
        this.weatherAPI = weatherAPI;
    }


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

            if(resp == null) {
                log.error("Response from weatherAPI is null");
                throw new LocationNotFoundException();
            }
            log.debug("Response from weatherAPI: {}", resp);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonWeather = mapper.readTree(resp);

            return parseJsonToDTO(jsonWeather);
        }  catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new APIResponseException();
        }
    }

    private static WeatherDTO parseJsonToDTO(JsonNode jsonWeather) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.convertValue(jsonWeather, WeatherDTO.class);
    }

}
