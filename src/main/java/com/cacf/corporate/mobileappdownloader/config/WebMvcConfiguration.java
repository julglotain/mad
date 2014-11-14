package com.cacf.corporate.mobileappdownloader.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
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

    private static final int CACHE_PERIOD = 3600 * 24 * 30;

    private static final long MAX_UPLOAD_SIZE = 100 * 1024 * 1024; // 100 MB

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {


        registry.addResourceHandler("/resources/**")
                .addResourceLocations("classpath:/frontend/")
                .setCachePeriod(CACHE_PERIOD);


    }

    @Bean
    public MappingJackson2HttpMessageConverter jackson2HttpMessageConverter() {
        return new MappingJackson2HttpMessageConverter();
    }

    @Bean
    public MultipartResolver multipartResolver() {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setMaxUploadSize(MAX_UPLOAD_SIZE);
        return resolver;
    }


}
