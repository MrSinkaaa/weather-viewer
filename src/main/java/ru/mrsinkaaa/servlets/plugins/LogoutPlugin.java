package ru.mrsinkaaa.servlets.plugins;

import ru.mrsinkaaa.servlets.ServletPlugin;
import ru.mrsinkaaa.utils.PathUtil;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LogoutPlugin implements ServletPlugin {


    @Override
    public boolean canHandle(String path) {
        return path.startsWith(PathUtil.LOGOUT);
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cookie cookie = new Cookie("session", null);
        cookie.setMaxAge(0);

        response.addCookie(cookie);
        request.getSession().invalidate();
        response.sendRedirect(PathUtil.LOGIN);
    }
}
