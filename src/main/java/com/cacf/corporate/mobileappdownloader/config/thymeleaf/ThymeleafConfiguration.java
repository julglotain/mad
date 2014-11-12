package com.cacf.corporate.mobileappdownloader.config.thymeleaf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by cacf on 09/10/14.
 */
@Configuration
public class ThymeleafConfiguration {

    @Bean
    public ThymeleafViewResolver thymeleafViewResolver() {
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine());
        viewResolver.setOrder(1);
        return viewResolver;
    }


    @Bean
    public SpringTemplateEngine templateEngine() {

        SpringTemplateEngine templateEngine = new SpringTemplateEngine();

        Set<TemplateResolver> templateResolvers = new HashSet<>();
        templateResolvers.add(templateViewsResolver());
        templateResolvers.add(templateManifestsResolver());
        templateEngine.setTemplateResolvers(templateResolvers);

        templateEngine.addDialect(new OTAAppInstallLinkDialect());

        return templateEngine;

    }

    @Bean
    public TemplateResolver templateViewsResolver() {

        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("frontend/views/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML5");
        return templateResolver;
    }

    @Bean
    public TemplateResolver templateManifestsResolver() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("manifests/");
        templateResolver.setSuffix(".plist");
        templateResolver.setTemplateMode("XML");
        return templateResolver;
    }

}
