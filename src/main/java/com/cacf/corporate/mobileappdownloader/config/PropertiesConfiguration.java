package com.cacf.corporate.mobileappdownloader.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;

import java.io.FileNotFoundException;

/**
 * Created by jug on 06/11/2014.
 */
@Configuration
public class PropertiesConfiguration {


    private static final Logger log = LoggerFactory.getLogger(ApplicationConfiguration.class);

    private static final String INTERNAL_CONFIG_PROPERTIES = "mad-config.properties";
    private static final String EXTERNAL_CONFIG_PROPERTIES_ENV_KEY = "mad.config.file";

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() throws FileNotFoundException {

        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();

        if (StringUtils.isEmpty(System.getProperty(EXTERNAL_CONFIG_PROPERTIES_ENV_KEY))) {

            log.info("Using internal config file.");

            configurer.setLocation(new ClassPathResource(INTERNAL_CONFIG_PROPERTIES));

        } else {

            log.info("Using external config file.");

            Resource externalConfig = new FileSystemResource(System.getProperty(EXTERNAL_CONFIG_PROPERTIES_ENV_KEY));

            if (externalConfig.exists()) {

                configurer.setLocation(externalConfig);

            } else {

                throw new FileNotFoundException("Specified external config file with path: " + System.getProperty(EXTERNAL_CONFIG_PROPERTIES_ENV_KEY) + " doesn't exist.");

            }

        }

        return configurer;

    }


}
