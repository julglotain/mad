package com.cacf.corporate.mobileappdownloader.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;

import java.io.FileNotFoundException;

/**
 * Created by jug on 18/11/2014.
 */
@Configuration
public class PropertiesTestConfiguration {

    private static final String INTERNAL_TEST_CONFIG_PROPERTIES = "mad-config-test.properties";

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() throws FileNotFoundException {

        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();

        configurer.setLocation(new ClassPathResource(INTERNAL_TEST_CONFIG_PROPERTIES));

        return configurer;

    }


}