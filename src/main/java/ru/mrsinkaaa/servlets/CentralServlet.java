package ru.mrsinkaaa.servlets;

import ru.mrsinkaaa.servlets.plugins.IndexPlugin;

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

    @Override
    public void init() {

        plugins.add(new IndexPlugin());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = request.getRequestURI();

        try {
            for(ServletPlugin plugin : plugins) {
                if(plugin.canHandle(path)) {
                    plugin.handle(request, response);
                    return;
                }
            }
        } catch (ServletException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database unavailable");
        }
    }
}
