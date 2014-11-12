package com.cacf.corporate.mobileappdownloader.config;


import com.cacf.corporate.mobileappdownloader.dto.store.AppVersion;
import com.cacf.corporate.mobileappdownloader.entities.store.Bundle;
import com.cacf.corporate.mobileappdownloader.mappers.AppVersionObjectFactory;
import ma.glasnost.orika.BoundMapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.inject.Inject;

/**
 * Created by jug on 22/10/2014.
 */
@Configuration
@ComponentScan("com.cacf.corporate.mobileappdownloader.mappers")
public class DataMappingConfiguration {

    @Inject
    private AppVersionObjectFactory appVersionObjectFactory;

    @Bean
    @Qualifier("bundlesMapperFacade")
    public BoundMapperFacade<Bundle, com.cacf.corporate.mobileappdownloader.dto.store.Bundle> mapperFacade() {

        MapperFactory factory = new DefaultMapperFactory.Builder().build();

        factory.classMap(Bundle.class, com.cacf.corporate.mobileappdownloader.dto.store.Bundle.class)
                .byDefault()
                .register();


        factory.registerObjectFactory(appVersionObjectFactory, AppVersion.class);

        return factory.getMapperFacade(Bundle.class, com.cacf.corporate.mobileappdownloader.dto.store.Bundle.class);
    }

}
