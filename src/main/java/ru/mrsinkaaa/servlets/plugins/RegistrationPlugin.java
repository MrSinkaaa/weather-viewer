package ru.mrsinkaaa.servlets.plugins;

import ru.mrsinkaaa.config.ThymeleafConfig;
import ru.mrsinkaaa.service.UserService;
import ru.mrsinkaaa.servlets.ServletPlugin;
import ru.mrsinkaaa.utils.PathUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ru.mrsinkaaa.servlets.CentralServlet.webContext;

public class RegistrationPlugin implements ServletPlugin {

    private static final UserService userService = UserService.getInstance();

    @Override
    public boolean canHandle(String path) {
        return path.startsWith(PathUtil.REGISTRATION);
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(request.getMethod().equals("GET")) {

            ThymeleafConfig.getTemplateEngine().process("registration.html", webContext, response.getWriter());
        } else {

            String login = request.getParameter("login");
            String password = request.getParameter("password");

            userService.register(login, password);
            response.sendRedirect(PathUtil.LOGIN);
        }

    }
}
