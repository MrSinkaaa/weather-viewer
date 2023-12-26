package ru.mrsinkaaa.servlets.plugins;

import ru.mrsinkaaa.dto.*;
import ru.mrsinkaaa.service.LocationService;
import ru.mrsinkaaa.service.SessionService;
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

    @Override
    public boolean canHandle(String path) {
        return path.startsWith(PathUtil.LOCATION);
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Optional<SessionDTO> session = sessionService.getSession(request);

        if (session.isPresent()) {
            int userId = session.get().getUserId();

            switch (request.getMethod()) {
                case "GET" -> getLocationByUser(userId);
                case "POST" -> saveLocation(request, userId);
                case "DELETE" -> deleteLocation(request, userId);
            }
        }

    }

    private void deleteLocation(HttpServletRequest request, int id) {
        int locationId = Integer.parseInt(request.getParameter("locationId"));

        locationService.findByLocationIdAndUserId(locationId, id)
                .ifPresent(locationService::delete);

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
    }

    private static void getLocationByUser(int id) {
        List<LocationDTO> savedLocations = locationService.findByUserId(id);
        webContext.setVariable("savedLocations", savedLocations);
    }


}
