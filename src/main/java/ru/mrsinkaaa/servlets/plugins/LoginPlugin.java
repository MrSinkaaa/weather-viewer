package ru.mrsinkaaa.servlets.plugins;

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

        String login = request.getParameter("login");
        String password = request.getParameter("password");

        userService.save(UserDTO.builder()
                .login(login)
                .password(password)
                .build());
    }
}
