package ru.mrsinkaaa.servlets.plugins;

import lombok.SneakyThrows;
import ru.mrsinkaaa.config.AppConfig;
import ru.mrsinkaaa.dto.UserDTO;
import ru.mrsinkaaa.exceptions.user.UserInputException;
import ru.mrsinkaaa.exceptions.user.UserNotFoundException;
import ru.mrsinkaaa.service.SessionService;
import ru.mrsinkaaa.service.UserService;
import ru.mrsinkaaa.utils.PathUtil;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

public class LoginPlugin extends BasePlugin {

    private static final String LOGIN_TEMPLATE = "authorization.html";
    private static final String USER_ATTRIBUTE = "user";
    private static final String LOGIN_ERROR = "/login?error=";

    @Override
    public boolean canHandle(String path) {
        return path.startsWith(PathUtil.LOGIN);
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (userAlreadyLoggedIn(request)) {
            response.sendRedirect(PathUtil.WEATHER);
        } else {
            if (isGetRequest(request)) {
                renderPage(response, LOGIN_TEMPLATE);
            } else {
                handleLoginRequest(request, response);
            }
        }
    }

    private void handleLoginRequest(HttpServletRequest request, HttpServletResponse response) {
        String login = request.getParameter("login");
        String password = request.getParameter("password");

        try {
            UserDTO userDTO = userService.login(login, password);
            onLoginSuccess(request, response, userDTO);
        } catch (UserInputException | UserNotFoundException e) {
            onLoginFail(response, e.getErrorMessage().getMessage());
        }
    }

    @SneakyThrows
    private void onLoginFail(HttpServletResponse response, String message) {
        response.sendRedirect(LOGIN_ERROR + message);
    }

    @SneakyThrows
    private void onLoginSuccess(HttpServletRequest request, HttpServletResponse response, UserDTO user) {
        UUID session = sessionService.createSession(user);
        Cookie cookie = new Cookie("session", session.toString());
        cookie.setMaxAge(Integer.parseInt(AppConfig.getProperty("session.expiresAt")) * 60);

        request.getSession().setAttribute(USER_ATTRIBUTE, user);
        response.addCookie(cookie);
        response.sendRedirect(PathUtil.WEATHER);
    }

}
