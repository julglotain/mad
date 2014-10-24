package com.cacf.corporate.mobileappdownloader.config;

import com.cacf.corporate.mobileappdownloader.bundles.domain.BundleConfiguration;
import com.cacf.corporate.mobileappdownloader.bundles.domain.BundlesStoreConfiguration;
import com.cacf.corporate.mobileappdownloader.bundles.domain.ProfileConfiguration;
import com.cacf.corporate.mobileappdownloader.bundles.dto.Application;
import com.cacf.corporate.mobileappdownloader.bundles.dto.Bundle;
import com.cacf.corporate.mobileappdownloader.bundles.dto.BundlesStore;
import com.cacf.corporate.mobileappdownloader.bundles.dto.Profile;
import com.cacf.corporate.mobileappdownloader.bundles.mapping.ApplicationObjectFactory;
import ma.glasnost.orika.BoundMapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.inject.Inject;

/**
 * Created by jug on 22/10/2014.
 */
@Configuration
public class DataMappingConfiguration {

    @Inject
    private ApplicationObjectFactory applicationObjectFactory;

    @Bean
    @Qualifier("bundlesConfigMapper")
    public BoundMapperFacade<BundlesStoreConfiguration, BundlesStore> mapperFacade() {

        MapperFactory factory = new DefaultMapperFactory.Builder().build();

        factory.classMap(BundlesStoreConfiguration.class, BundlesStore.class)
                .field("bundlesList", "bundles").register();

        factory.classMap(BundleConfiguration.class, Bundle.class)
                .field("profilesList", "profiles")
                .byDefault()
                .register();

        factory.classMap(ProfileConfiguration.class, Profile.class)
                .field("appsConfigList", "applications")
                .byDefault()
                .register();

        factory.registerObjectFactory(applicationObjectFactory, Application.class);

        return factory.getMapperFacade(BundlesStoreConfiguration.class, BundlesStore.class);
    }

}
