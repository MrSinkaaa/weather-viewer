package ru.mrsinkaaa.servlets.plugins;

import lombok.SneakyThrows;
import ru.mrsinkaaa.service.ImageService;
import ru.mrsinkaaa.servlets.ServletPlugin;
import ru.mrsinkaaa.utils.PathUtil;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

public class ImagePlugin implements ServletPlugin {

    private final ImageService imageService = ImageService.getInstance();


    @Override
    public boolean canHandle(String path) {
        return path.startsWith(PathUtil.IMAGES);
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String imagePath = request.getRequestURI();

        imageService.get(imagePath)
                .ifPresentOrElse(image -> {
                    response.setContentType("application/octet-stream");
                    writeImage(response, image);
                }, () -> response.setStatus(HttpServletResponse.SC_NOT_FOUND));

    }

    @SneakyThrows
    private void writeImage(HttpServletResponse response, InputStream image) {
        try (image; var outputStream = response.getOutputStream()){
            int currentByte;
            while ((currentByte = image.read())!= -1) {
                outputStream.write(currentByte);
            }
        }
    }
}
