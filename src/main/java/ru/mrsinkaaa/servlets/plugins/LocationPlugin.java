package ru.mrsinkaaa.servlets.plugins;

import ru.mrsinkaaa.dto.LocationDTO;
import ru.mrsinkaaa.dto.SessionDTO;
import ru.mrsinkaaa.dto.WeatherDTO;
import ru.mrsinkaaa.service.LocationService;
import ru.mrsinkaaa.service.SessionService;
import ru.mrsinkaaa.service.WeatherService;
import ru.mrsinkaaa.servlets.ServletPlugin;
import ru.mrsinkaaa.utils.PathUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static ru.mrsinkaaa.servlets.CentralServlet.webContext;

public class LocationPlugin implements ServletPlugin {

    private static final LocationService locationService = LocationService.getInstance();
    private static final SessionService sessionService = SessionService.getInstance();
    private static final WeatherService weatherService = WeatherService.getInstance();

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

        locationService.findByLocationName(location)
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

    private static void getLocationByUser(int id) {
        List<WeatherDTO> savedLocations = locationService.findByUserId(id)
                .stream().map(location ->
                        weatherService.getWeather(location.getName()))
                .toList();
        webContext.setVariable("savedLocations", savedLocations);
    }


}
