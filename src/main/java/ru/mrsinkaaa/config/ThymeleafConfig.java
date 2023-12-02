package ru.mrsinkaaa.config;

import lombok.Getter;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;


public class ThymeleafConfig {

    @Getter
    private static final TemplateEngine templateEngine;

    static {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();

        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setPrefix("/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setCacheable(false);

        templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
    }

}
