package com.cacf.corporate.mobileappdownloader.config;

import com.cacf.corporate.mobileappdownloader.schedulers.AccessTokenManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by cacf on 09/10/14.
 */
@Configuration
@EnableScheduling
@ComponentScan("com.cacf.corporate.mobileappdownloader.schedulers")
public class SchedulerConfiguration {

    @Bean
    public AccessTokenManager accessTokenManager(){
        return new AccessTokenManager();
    }

}
