package ru.mrsinkaaa.servlets.plugins;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.thymeleaf.context.WebContext;
import ru.mrsinkaaa.api.WeatherAPI;
import ru.mrsinkaaa.config.AppConfig;
import ru.mrsinkaaa.config.ThymeleafConfig;
import ru.mrsinkaaa.dto.WeatherDTO;
import ru.mrsinkaaa.servlets.ServletPlugin;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class WeatherPlugin implements ServletPlugin {

    private final WeatherAPI weatherAPI = WeatherAPI.getInstance();

    @Override
    public boolean canHandle(String path) {
        return path.startsWith("/weather");
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");

        String city = request.getParameter("city");
        String url = AppConfig.getProperty("api.url.city").formatted(city, AppConfig.getProperty("api.key"));

        try {
            String resp = weatherAPI.sendGetRequest(url);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonWeather = mapper.readTree(resp);

            WeatherDTO weatherDTO = WeatherDTO.builder()
                    .longitude(BigDecimal.valueOf(jsonWeather.get("coord").get("lon").asDouble()))
                    .latitude(BigDecimal.valueOf(jsonWeather.get("coord").get("lat").asDouble()))
                    .city(jsonWeather.get("name").asText())
                    .country(jsonWeather.get("sys").get("country").asText())
                    .weatherIcon(jsonWeather.get("weather").get(0).get("icon").asText())
                    .sunrise(parseUTCtoLocalDateTime(jsonWeather.get("sys").get("sunrise").asText()))
                    .sunset(parseUTCtoLocalDateTime(jsonWeather.get("sys").get("sunset").asText()))
                    .temperature(Double.valueOf(jsonWeather.get("main").get("temp").asText()))
                    .humidity(Double.valueOf(jsonWeather.get("main").get("humidity").asText()))
                    .pressure(Double.valueOf(jsonWeather.get("main").get("pressure").asText()))
                    .visibility(Double.valueOf(jsonWeather.get("visibility").asText()))
                    .windSpeed(Double.valueOf(jsonWeather.get("wind").get("speed").asText()))
                    .windDeg(Double.valueOf(jsonWeather.get("wind").get("deg").asText()))
                    .build();

            WebContext webContext = new WebContext(request, response, request.getServletContext(), request.getLocale());
            webContext.setVariable("weather", weatherDTO);

            ThymeleafConfig.getTemplateEngine().process("weather", webContext, response.getWriter());
        } catch (RuntimeException e) {
            throw new RuntimeException("Something went wrong", e);
        }




    }

    private static LocalDateTime parseUTCtoLocalDateTime(String unixTimestamp) {
        Instant instant = Instant.ofEpochSecond(Long.parseLong(unixTimestamp));

        // Convert the Instant to LocalDateTime using the system default time zone
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

        return localDateTime;
    }
}
