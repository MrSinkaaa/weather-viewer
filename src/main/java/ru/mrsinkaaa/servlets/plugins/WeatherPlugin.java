package ru.mrsinkaaa.servlets.plugins;

import ru.mrsinkaaa.api.WeatherAPI;
import ru.mrsinkaaa.config.AppConfig;
import ru.mrsinkaaa.servlets.ServletPlugin;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class WeatherPlugin implements ServletPlugin {

    private final WeatherAPI weatherAPI = WeatherAPI.getInstance();

    @Override
    public boolean canHandle(String path) {
        return path.startsWith("/weather");
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String city = request.getParameter("city");
        String url = AppConfig.getProperty("api.url.city").formatted(city, AppConfig.getProperty("api.key"));

        try {
            String resp = weatherAPI.sendGetRequest(url);
            response.getWriter().println(resp);
        } catch (RuntimeException e) {
            throw new RuntimeException("Something went wrong", e);
        }

        request.getSession().setAttribute("city", city);
    }
}
