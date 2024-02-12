package ru.mrsinkaaa.service;

import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import ru.mrsinkaaa.config.AppConfig;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@NoArgsConstructor()
public class ImageService {

    private final String basePath = AppConfig.getProperty("image.base.url");

    @SneakyThrows
    public Optional<InputStream> get(String imagePath) {
        Path imageFullPath = Path.of(basePath, imagePath);

        return Files.exists(imageFullPath)
                ? Optional.of(Files.newInputStream(imageFullPath))
                : Optional.empty();
    }
}
