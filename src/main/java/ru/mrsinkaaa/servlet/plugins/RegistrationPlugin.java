package ru.mrsinkaaa.servlet.plugins;

import lombok.extern.log4j.Log4j2;
import ru.mrsinkaaa.exception.user.UserAlreadyExistsException;
import ru.mrsinkaaa.exception.user.UserInputException;
import ru.mrsinkaaa.utils.PathUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
public class RegistrationPlugin extends BasePlugin {

    private static final String REGISTRATION_TEMPLATE = "authorization.html";
    private static final String REGISTRATION_ERROR = "/registration?error=";

    @Override
    public boolean canHandle(String path) {
        return path.startsWith(PathUtil.REGISTRATION);
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if(userAlreadyLoggedIn(request)) {
            response.sendRedirect(PathUtil.WEATHER);
        } else {
            if(isGetRequest(request)) {
                renderPage(response, REGISTRATION_TEMPLATE);
            } else {
                handleRegistrationRequest(request, response);
            }
        }

    }

    private void handleRegistrationRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String login = request.getParameter("login");
        String password = request.getParameter("password");

        try {
            userService.register(login, password);
            response.sendRedirect(PathUtil.LOGIN);
        } catch (UserInputException | UserAlreadyExistsException e) {
            log.warn(e.getErrorMessage().getMessage());
            response.sendRedirect(REGISTRATION_ERROR + e.getErrorMessage().getMessage());
        }
    }

}
