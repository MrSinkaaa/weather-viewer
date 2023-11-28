package ru.mrsinkaaa.servlets.plugins;

import ru.mrsinkaaa.servlets.ServletPlugin;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginPlugin implements ServletPlugin {

    @Override
    public boolean canHandle(String path) {
        return path.startsWith("/login");
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
