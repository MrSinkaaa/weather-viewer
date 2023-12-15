package ru.mrsinkaaa.servlets.plugins;

import jakarta.persistence.NoResultException;
import lombok.SneakyThrows;
import org.thymeleaf.context.WebContext;
import ru.mrsinkaaa.config.ThymeleafConfig;
import ru.mrsinkaaa.dto.UserDTO;
import ru.mrsinkaaa.service.UserService;
import ru.mrsinkaaa.servlets.ServletPlugin;
import ru.mrsinkaaa.utils.PathUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ru.mrsinkaaa.servlets.CentralServlet.webContext;

public class LoginPlugin implements ServletPlugin {

    private static final UserService userService = UserService.getInstance();

    @Override
    public boolean canHandle(String path) {
        return path.startsWith(PathUtil.LOGIN);
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getMethod().equals("GET")) {

            ThymeleafConfig.getTemplateEngine().process("login.html", webContext, response.getWriter());
        } else {
            try {
                userService.login(request.getParameter("login"), request.getParameter("password"))
                        .ifPresent(user -> onLoginSuccess(request, response, user));
            } catch (NoResultException e) {
                onLoginFail(request, response);
            }
        }

    }

    @SneakyThrows
    private void onLoginFail(HttpServletRequest request, HttpServletResponse response) {
        response.sendRedirect("/login?error&login=" + request.getParameter("login"));
    }

    @SneakyThrows
    private void onLoginSuccess(HttpServletRequest request, HttpServletResponse response, UserDTO user) {
        request.getSession().setAttribute("user", user);
        response.sendRedirect("/weather");
    }

}
