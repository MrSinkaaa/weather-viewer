package ru.mrsinkaaa.service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import ru.mrsinkaaa.config.AppConfig;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;


@NoArgsConstructor
public class StyleService {


    private final String basePath = AppConfig.getProperty("style.base.url");

    @SneakyThrows
    public Optional<InputStream> get(String stylePath) {
        Path styleFullPath = Path.of(basePath, stylePath);

        return Files.exists(styleFullPath)
                ? Optional.of(Files.newInputStream(styleFullPath))
                : Optional.empty();
    }

}
