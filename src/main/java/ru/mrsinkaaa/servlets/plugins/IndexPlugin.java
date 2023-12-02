package ru.mrsinkaaa.servlets.plugins;

import org.thymeleaf.context.WebContext;
import ru.mrsinkaaa.config.ThymeleafConfig;
import ru.mrsinkaaa.servlets.ServletPlugin;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class IndexPlugin implements ServletPlugin {

    @Override
    public boolean canHandle(String path) {
        return path.equals("/");
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        WebContext webContext = new WebContext(request, response, request.getServletContext(), request.getLocale());

        ThymeleafConfig.getTemplateEngine().process("index.html", webContext, response.getWriter());

    }
}
