package ru.mrsinkaaa.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface ServletPlugin {

    void init();
    boolean canHandle(String path);
    void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
}
