package ru.mrsinkaaa.servlet.plugins;

import lombok.extern.log4j.Log4j2;
import ru.mrsinkaaa.api.WeatherAPI;
import ru.mrsinkaaa.dto.UserDTO;
import ru.mrsinkaaa.dto.WeatherDTO;
import ru.mrsinkaaa.exception.api.APIResponseException;
import ru.mrsinkaaa.exception.location.LocationNotFoundException;
import ru.mrsinkaaa.repository.LocationRepository;
import ru.mrsinkaaa.service.LocationService;
import ru.mrsinkaaa.service.WeatherService;
import ru.mrsinkaaa.utils.PathUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static ru.mrsinkaaa.servlet.CentralServlet.webContext;

@Log4j2
public class WeatherPlugin extends BasePlugin {

    private static final String USER_ATTRIBUTE = "user";
    private static final String WEATHER_TEMPLATE = "weather.html";
    private static final String ERROR_REDIRECT = "/weather?error=";

    private LocationService locationService;
    private WeatherService weatherService;

    @Override
    public void init() {
        this.locationService = new LocationService(new LocationRepository());
        this.weatherService = new WeatherService(new WeatherAPI());
    }

    @Override
    public boolean canHandle(String path) {
        return path.startsWith(PathUtil.WEATHER);
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) throws IOException {

        if (userAlreadyLoggedIn(request)) {
            handleWeatherRequest(request, response);
        } else {
            response.sendRedirect(PathUtil.LOGIN);
        }
    }

    private void handleWeatherRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserDTO user = (UserDTO) request.getSession().getAttribute(USER_ATTRIBUTE);
        webContext.setVariable(USER_ATTRIBUTE, user);

        if (request.getParameter("city") != null) {
            processWeatherRequest(request, response, user);
        }

        renderPage(response, WEATHER_TEMPLATE);
    }

    private void processWeatherRequest(HttpServletRequest request, HttpServletResponse response, UserDTO user) throws IOException {
        String city = request.getParameter("city");
        try {
            WeatherDTO weatherDTO = weatherService.getWeather(city);
            webContext.setVariable("weather", weatherDTO);
        } catch (APIResponseException | LocationNotFoundException e) {
            log.error("Error processing weather request: {}", e.getMessage());
            webContext.setVariable("error", e.getErrorMessage().getMessage());

        } finally {
            List<WeatherDTO> savedLocations = getSavedLocations(user);
            webContext.setVariable("savedLocations", savedLocations);
        }
    }

    private List<WeatherDTO> getSavedLocations(UserDTO user) {
        return locationService.findByUserId(user.getId())
                .stream().map(location ->
                        weatherService.getWeather(location.getName()))
                .toList();
    }

}
