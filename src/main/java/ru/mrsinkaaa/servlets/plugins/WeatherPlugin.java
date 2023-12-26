package ru.mrsinkaaa.servlets.plugins;

import ru.mrsinkaaa.config.ThymeleafConfig;
import ru.mrsinkaaa.dto.LocationDTO;
import ru.mrsinkaaa.dto.SessionDTO;
import ru.mrsinkaaa.dto.UserDTO;
import ru.mrsinkaaa.dto.WeatherDTO;
import ru.mrsinkaaa.service.LocationService;
import ru.mrsinkaaa.service.SessionService;
import ru.mrsinkaaa.service.WeatherService;
import ru.mrsinkaaa.servlets.ServletPlugin;
import ru.mrsinkaaa.utils.PathUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static ru.mrsinkaaa.servlets.CentralServlet.webContext;

public class WeatherPlugin implements ServletPlugin {

    private final SessionService sessionService = SessionService.getInstance();
    private final LocationService locationService = LocationService.getInstance();
    private final WeatherService weatherService = WeatherService.getInstance();

    @Override
    public boolean canHandle(String path) {
        return path.startsWith(PathUtil.WEATHER);
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Optional<SessionDTO> session = sessionService.getSession(request);

        if(session.isPresent()) {
            UserDTO user = (UserDTO) request.getSession().getAttribute("user");
            webContext.setVariable("user", user);

            if (request.getMethod().equals("GET")) {
                ThymeleafConfig.getTemplateEngine().process("weather.html", webContext, response.getWriter());
            }
            if (request.getParameter("city") != null) {
                try {
                    String city = request.getParameter("city");
                    WeatherDTO weatherDTO = weatherService.getWeather(city);

                    List<WeatherDTO> savedLocations = locationService.findByUserId(user.getId())
                            .stream().map(location ->
                                    weatherService.getWeather(location.getName()))
                            .toList();

                    webContext.setVariable("savedLocations", savedLocations);
                    webContext.setVariable("weather", weatherDTO);

                    ThymeleafConfig.getTemplateEngine().process("weather.html", webContext, response.getWriter());
                } catch (RuntimeException | IOException e) {
                    response.sendRedirect("/weather?error");
                    System.out.println(e.getMessage());
                }
            }
        }
    }



}
