package com.cacf.corporate.mobileappdownloader.bundles;

import com.cacf.corporate.mobileappdownloader.bundles.domain.BundleConfigurationRepository;
import com.cacf.corporate.mobileappdownloader.bundles.domain.BundlesStoreConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by jug on 20/10/2014.
 */
public class BundlesStoreConfigurationManager {

    private static final Logger log = LoggerFactory.getLogger(BundlesStoreConfigurationManager.class);

    private BundleConfigurationRepository repository;

    public BundlesStoreConfigurationManager(BundleConfigurationRepository repo) {
        this.repository = repo;
    }

    public void loadConfiguration() throws IOException {
        synchronized (getConfig()) {
            this.repository.loadConfiguration();
        }
    }

    public BundlesStoreConfiguration getConfig() {
        return this.repository.getConfig();
    }

    public void setConfig(BundlesStoreConfiguration config) {
        this.repository.setConfig(config);
    }

    public void persistConfig(BundlesStoreConfiguration config) {

        synchronized (getConfig()) {

            this.repository.persistConfig(config);

        }

    }

}
