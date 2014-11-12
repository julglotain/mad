package com.cacf.corporate.mobileappdownloader.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by cacf on 09/10/14.
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.cacf.corporate.mobileappdownloader.controllers")
public class WebMvcConfiguration extends WebMvcConfigurerAdapter {

    private static final int CACHE_PERIOD = 3600;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/")
                .setCachePeriod(CACHE_PERIOD);

        registry.addResourceHandler("/js/**")
                .addResourceLocations("classpath:/frontend/js/")
                .setCachePeriod(CACHE_PERIOD);


    }

    @Bean
    public MappingJackson2HttpMessageConverter jackson2HttpMessageConverter() {
        return new MappingJackson2HttpMessageConverter();
    }

    @Bean
    public MultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }


}
