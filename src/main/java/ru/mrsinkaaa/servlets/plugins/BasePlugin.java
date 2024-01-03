package ru.mrsinkaaa.servlets.plugins;

import ru.mrsinkaaa.config.ThymeleafConfig;
import ru.mrsinkaaa.service.SessionService;
import ru.mrsinkaaa.servlets.ServletPlugin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ru.mrsinkaaa.servlets.CentralServlet.webContext;

public abstract class BasePlugin implements ServletPlugin {

    private static final SessionService sessionService = SessionService.getInstance();

    boolean userAlreadyLoggedIn(HttpServletRequest request) {
        return sessionService.getSession(request).isPresent();
    }

    boolean isGetRequest(HttpServletRequest request) {
        return "GET".equalsIgnoreCase(request.getMethod());
    }

    void renderPage(HttpServletResponse response, String template) throws IOException {
        ThymeleafConfig.getTemplateEngine().process(template, webContext, response.getWriter());
    }
}
