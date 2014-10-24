package com.cacf.corporate.mobileappdownloader.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

/**
 * Created by jug on 21/10/2014.
 */
@Configuration
public class ResourcesConfiguration {

    private static final Logger log = LoggerFactory.getLogger(ResourcesConfiguration.class);

    @Bean
    @Qualifier("usersStore")
    public Resource userStoreResource(@Value("${mad.users-store.resource.uri}") String usersStoreURI) {

        log.debug("Using '{}' as users store.", usersStoreURI);

        return new PathMatchingResourcePatternResolver().getResource(usersStoreURI);
    }

    @Bean
    @Qualifier("bundlesAppsStore")
    public Resource bundlesAppsStoreResource(@Value("${mad.apps-bundles-store.resource.uri}") String appsBundlesStoreURI) {

        log.debug("Using '{}' as bundles apps store.", appsBundlesStoreURI);

        return new PathMatchingResourcePatternResolver().getResource(appsBundlesStoreURI);
    }


}
