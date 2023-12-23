package ru.mrsinkaaa.servlets.plugins;

import lombok.SneakyThrows;
import ru.mrsinkaaa.service.StyleService;
import ru.mrsinkaaa.servlets.ServletPlugin;
import ru.mrsinkaaa.utils.PathUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

public class StylePlugin implements ServletPlugin {

    private final StyleService styleService = StyleService.getInstance();

    @Override
    public boolean canHandle(String path) {
        return path.startsWith(PathUtil.STYLES);
    }


    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String stylePath = request.getRequestURI();

        getStyle(response, stylePath);
    }

    private void getStyle(HttpServletResponse response, String stylePath) {
        styleService.get(stylePath)
                .ifPresentOrElse(style -> {
                    response.setContentType("text/css");
                    writeStyle(response, style);
                }, () -> response.setStatus(HttpServletResponse.SC_NOT_FOUND));
    }

    @SneakyThrows
    private void writeStyle(HttpServletResponse response, InputStream style) {
        try(style; var outputStream = response.getOutputStream()) {
            int currentByte;
            while ((currentByte = style.read())!= -1) {
                outputStream.write(currentByte);
            }
        }
    }
}
