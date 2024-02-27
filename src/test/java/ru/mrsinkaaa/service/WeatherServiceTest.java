package ru.mrsinkaaa.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import ru.mrsinkaaa.api.WeatherAPI;
import ru.mrsinkaaa.dto.WeatherDTO;
import ru.mrsinkaaa.exception.api.APIResponseException;
import ru.mrsinkaaa.exception.location.LocationNotFoundException;

import java.io.IOException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WeatherServiceTest {

    @Mock
    private WeatherAPI weatherAPI;

    @InjectMocks
    private WeatherService weatherService;


    // Mock API response
    String mockApiResponse = """
                {
                  "name": "Moscow",
                  "coord": {
                    "lon": 55.75,
                    "lat": 37.61
                  }
                }
                """;

    @BeforeEach
    public void setUp() {
        this.weatherAPI = Mockito.mock(WeatherAPI.class);
        weatherService = new WeatherService(weatherAPI);
    }

    @Test
    void getWeatherByCity_Success() throws IOException {
        when(weatherAPI.sendGetRequest(anyString())).thenReturn(mockApiResponse);

        WeatherDTO result = weatherService.getWeather("Moscow");

        assertNotNull(result);
        assertEquals("Moscow", result.getCity());
        assertEquals(BigDecimal.valueOf(55.75), result.getLongitude());
        assertEquals(BigDecimal.valueOf(37.61), result.getLatitude());
    }

    @Test
    void getWeatherByCoords_Success() throws IOException {
        when(weatherAPI.sendGetRequest(anyString())).thenReturn(mockApiResponse);

        WeatherDTO result = weatherService.getWeather(BigDecimal.valueOf(55.75), BigDecimal.valueOf(37.61));

        assertNotNull(result);
        assertEquals("Moscow", result.getCity());
        assertEquals(BigDecimal.valueOf(55.75), result.getLongitude());
        assertEquals(BigDecimal.valueOf(37.61), result.getLatitude());
    }

    @Test
    void getWeatherLocation_NotFoundException() throws IOException {
        assertThrows(LocationNotFoundException.class, () -> weatherService.getWeather("Unknown"));
    }

}
