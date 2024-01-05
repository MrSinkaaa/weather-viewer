package ru.mrsinkaaa.servlets.plugins;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;



public class IndexPlugin extends BasePlugin {

    public static final String HEADER_TEMPLATE = "header.html";

    @Override
    public boolean canHandle(String path) {
        return path.equals("/");
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        renderPage(response, HEADER_TEMPLATE);
    }
}
