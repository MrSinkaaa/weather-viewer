package ru.mrsinkaaa.servlet.plugins;

import ru.mrsinkaaa.config.ThymeleafConfig;
import ru.mrsinkaaa.repository.SessionRepository;
import ru.mrsinkaaa.repository.UserRepository;
import ru.mrsinkaaa.service.SessionService;
import ru.mrsinkaaa.service.UserService;
import ru.mrsinkaaa.servlet.ServletPlugin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ru.mrsinkaaa.servlet.CentralServlet.webContext;

public abstract class BasePlugin implements ServletPlugin {


    protected static SessionRepository sessionRepository;
    protected static SessionService sessionService;
    protected static UserRepository userRepository;
    protected static UserService userService;

    public void init() {
        userRepository = new UserRepository();
        sessionRepository = new SessionRepository();

        userService = new UserService(userRepository);
        sessionService = new SessionService(sessionRepository);
    }

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
