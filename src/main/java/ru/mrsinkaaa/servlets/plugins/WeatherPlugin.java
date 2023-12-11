package ru.mrsinkaaa.servlets.plugins;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.mrsinkaaa.api.WeatherAPI;
import ru.mrsinkaaa.config.AppConfig;
import ru.mrsinkaaa.config.ThymeleafConfig;
import ru.mrsinkaaa.dto.UserDTO;
import ru.mrsinkaaa.dto.WeatherDTO;
import ru.mrsinkaaa.servlets.ServletPlugin;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static ru.mrsinkaaa.servlets.CentralServlet.webContext;

public class WeatherPlugin implements ServletPlugin {

    private final WeatherAPI weatherAPI = WeatherAPI.getInstance();

    @Override
    public boolean canHandle(String path) {
        return path.startsWith("/weather");
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserDTO user = (UserDTO) request.getSession().getAttribute("user");
        webContext.setVariable("user", user);

        if (request.getMethod().equals("GET")) {

            ThymeleafConfig.getTemplateEngine().process("weather.html", webContext, response.getWriter());
        }
        if (request.getParameter("city") != null) {

            String city = request.getParameter("city");
            String url = AppConfig.getProperty("api.url.city").formatted(city, AppConfig.getProperty("api.key"));
            try {
                WeatherDTO weatherDTO = getWeatherDTO(url);

                webContext.setVariable("weather", weatherDTO);

                ThymeleafConfig.getTemplateEngine().process("weather.html", webContext, response.getWriter());
            } catch (RuntimeException e) {
                response.sendRedirect("/weather?error");
                System.out.println(e.getMessage());
            }
        }
    }

    private WeatherDTO getWeatherDTO(String url) throws IOException {
        String resp = weatherAPI.sendGetRequest(url);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonWeather = mapper.readTree(resp);

        return WeatherDTO.builder()
                .longitude(BigDecimal.valueOf(jsonWeather.get("coord").get("lon").asDouble()))
                .latitude(BigDecimal.valueOf(jsonWeather.get("coord").get("lat").asDouble()))
                .city(jsonWeather.get("name").asText())
                .country(jsonWeather.get("sys").get("country").asText())
                .weatherIcon(jsonWeather.get("weather").get(0).get("icon").asText())
                .sunrise(parseUTCtoLocalDateTime(jsonWeather.get("sys").get("sunrise").asLong()))
                .sunset(parseUTCtoLocalDateTime(jsonWeather.get("sys").get("sunset").asLong()))
                .temperature(convertTempToCelsius(jsonWeather.get("main").get("temp").asDouble()))
                .humidity(Double.valueOf(jsonWeather.get("main").get("humidity").asText()))
                .pressure(Double.valueOf(jsonWeather.get("main").get("pressure").asText()))
                .visibility(Double.valueOf(jsonWeather.get("visibility").asText()))
                .windSpeed(Double.valueOf(jsonWeather.get("wind").get("speed").asText()))
                .windDeg(Double.valueOf(jsonWeather.get("wind").get("deg").asText()))
                .build();
    }

    private static Double convertTempToCelsius(Double temp) {
        return Math.ceil(temp - 273.15);
    }

    private static String parseUTCtoLocalDateTime(Long unixTimestamp) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy");

        Instant instant = Instant.ofEpochSecond(unixTimestamp);

        // Convert the Instant to LocalDateTime using the system default time zone

        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).format(formatter);
    }
}
