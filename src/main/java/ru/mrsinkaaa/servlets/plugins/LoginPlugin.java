package ru.mrsinkaaa.servlets.plugins;

import lombok.SneakyThrows;
import org.thymeleaf.context.WebContext;
import ru.mrsinkaaa.config.ThymeleafConfig;
import ru.mrsinkaaa.dto.UserDTO;
import ru.mrsinkaaa.service.UserService;
import ru.mrsinkaaa.servlets.ServletPlugin;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginPlugin implements ServletPlugin {

    private static final UserService userService = UserService.getInstance();

    @Override
    public boolean canHandle(String path) {
        return path.startsWith("/login");
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getMethod().equals("GET")) {
            WebContext webContext = new WebContext(request, response, request.getServletContext(), request.getLocale());

            ThymeleafConfig.getTemplateEngine().process("login.html", webContext, response.getWriter());
        } else {
            userService.login(request.getParameter("login"), request.getParameter("password"))
                    .ifPresentOrElse(
                            user -> onLoginSuccess(request, response, user),
                            () -> onLoginFail(request, response));
        }

    }

    @SneakyThrows
    private void onLoginFail(HttpServletRequest request, HttpServletResponse response) {
        response.sendRedirect("/login?error&email=" + request.getParameter("login"));
    }

    @SneakyThrows
    private void onLoginSuccess(HttpServletRequest request, HttpServletResponse response, UserDTO user) {
        WebContext webContext = new WebContext(request, response, request.getServletContext(), request.getLocale());
        webContext.setVariable("user", user);
        response.sendRedirect("/weather");
    }

}
