package ru.mrsinkaaa.service;

import lombok.SneakyThrows;
import ru.mrsinkaaa.config.AppConfig;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public class ImageService {

    private static final ImageService INSTANCE = new ImageService();

    private final String basePath = AppConfig.getProperty("image.base.url");

    private ImageService() {
    }



    public static ImageService getInstance() {
        return INSTANCE;
    }

    @SneakyThrows
    public Optional<InputStream> get(String imagePath) {
        Path imageFullPath = Path.of(basePath, imagePath);

        return Files.exists(imageFullPath)
                ? Optional.of(Files.newInputStream(imageFullPath))
                : Optional.empty();
    }
}
