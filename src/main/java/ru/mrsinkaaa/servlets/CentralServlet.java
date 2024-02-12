package ru.mrsinkaaa.servlets;

import org.thymeleaf.context.WebContext;
import ru.mrsinkaaa.servlets.plugins.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/")
public class CentralServlet extends HttpServlet {

    private final List<ServletPlugin> plugins = new ArrayList<>();

    public static WebContext webContext;

    @Override
    public void init() {

        plugins.add(new IndexPlugin());
        plugins.add(new ImagePlugin());
        plugins.add(new StylePlugin());

        plugins.add(new LoginPlugin());
        plugins.add(new RegistrationPlugin());
        plugins.add(new LogoutPlugin());

        plugins.add(new WeatherPlugin());
        plugins.add(new LocationPlugin());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = request.getRequestURI();
        webContext = new WebContext(request, response, getServletContext(), request.getLocale());

        try {
            for(ServletPlugin plugin : plugins) {
                if(plugin.canHandle(path)) {
                    plugin.init();
                    plugin.handle(request, response);
                    return;
                }
            }
        } catch (ServletException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database unavailable");
        }
    }
}
