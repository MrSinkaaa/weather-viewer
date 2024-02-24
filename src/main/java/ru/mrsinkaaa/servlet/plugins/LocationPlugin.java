package ru.mrsinkaaa.servlet.plugins;

import ru.mrsinkaaa.api.WeatherAPI;
import ru.mrsinkaaa.dto.LocationDTO;
import ru.mrsinkaaa.dto.SessionDTO;
import ru.mrsinkaaa.dto.WeatherDTO;
import ru.mrsinkaaa.repository.LocationRepository;
import ru.mrsinkaaa.service.LocationService;
import ru.mrsinkaaa.service.WeatherService;
import ru.mrsinkaaa.utils.PathUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static ru.mrsinkaaa.servlet.CentralServlet.webContext;

public class LocationPlugin extends BasePlugin {

    private LocationService locationService;
    private WeatherService weatherService;

    @Override
    public void init() {
        super.init();
        locationService = new LocationService(new LocationRepository());
        weatherService = new WeatherService(new WeatherAPI());
    }

    @Override
    public boolean canHandle(String path) {
        return path.startsWith(PathUtil.LOCATION);
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Optional<SessionDTO> session = sessionService.getSession(request);

        if (session.isPresent()) {
            int userId = session.get().getUserId();

            if(request.getParameter("deleteLocation") != null) {
                deleteLocation(request, userId);
            } else {
                saveLocation(request, userId);
            }

            request.getRequestDispatcher(PathUtil.WEATHER + "?city=" + request.getParameter("city"))
                    .forward(request, response);
        } else {
            response.sendRedirect(PathUtil.LOGIN);
        }
    }

    private void deleteLocation(HttpServletRequest request, int id) {
        String location = request.getParameter("deleteLocation");

        locationService.findByLocationName(location).stream()
                .filter(loc -> loc.getUserId() == id)
                .findFirst()
                .ifPresent(locationService::delete);

        getLocationByUser(id);
    }

    private void saveLocation(HttpServletRequest request, int id) {
        String city = request.getParameter("city");
        String latitude = request.getParameter("latitude");
        String longitude = request.getParameter("longitude");

        LocationDTO locationDTO = LocationDTO.builder()
                .name(city)
                .userId(id)
                .longitude(BigDecimal.valueOf(Double.parseDouble(longitude)))
                .latitude(BigDecimal.valueOf(Double.parseDouble(latitude)))
                .build();

        locationService.save(locationDTO);
        getLocationByUser(id);
    }


    private void getLocationByUser(int id) {
        List<WeatherDTO> savedLocations = locationService.findByUserId(id)
                .stream().map(location ->
                        weatherService.getWeather(location.getName()))
                .toList();

        webContext.setVariable("savedLocations", savedLocations);
    }


}
