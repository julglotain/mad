package com.cacf.corporate.mobileappdownloader.repositories;

import com.cacf.corporate.mobileappdownloader.entities.store.AppVersion;
import com.cacf.corporate.mobileappdownloader.entities.store.AppsStore;
import com.cacf.corporate.mobileappdownloader.entities.store.Bundle;
import com.cacf.corporate.mobileappdownloader.utils.Pair;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.oxm.xstream.XStreamMarshaller;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by jug on 06/11/2014.
 */
@Component
public class XmlFileAppsStoreRepository implements AppsStoreRepository {

    private static final Logger log = LoggerFactory.getLogger(XmlFileAppsStoreRepository.class);

    // holder for appsStore configuration
    private AppsStore appsStoreHolder;

    @Inject
    private XStreamMarshaller marshaller;

    @Inject
    @Qualifier("bundlesAppsStore")
    private Resource storeSource;

    private void loadConfiguration() throws IOException {

        log.debug("Load apps store configuration from {}", storeSource.getFile().getAbsolutePath());

        // this.appsStoreHolder = (AppsStore) marshaller.getXStream().fromXML(storeSource.getFile());
        this.appsStoreHolder = (AppsStore) marshaller.unmarshalInputStream(new FileInputStream(storeSource.getFile()));
    }

    @Override
    public AppsStore getConfig() {
        if (appsStoreHolder == null) {
            try {
                loadConfiguration();
            } catch (IOException e) {
                log.error("Erreur lors de la récupération de la configuration des bundles d'apps.", e);
            }
        }
        return appsStoreHolder;
    }

    @Override
    public void forceReloading() {
        try {
            loadConfiguration();
        } catch (IOException e) {
            log.error("Erreur lors de la récupération de la configuration des bundles d'apps.", e);
        }
    }

    @Override
    public void setConfig(AppsStore config) {
        this.appsStoreHolder = config;
    }

    @Override
    public synchronized void persistConfig(AppsStore config) {

        synchronized (getConfig()) {

            try (FileOutputStream outputStream = new FileOutputStream(storeSource.getFile())) {

                IOUtils.copy(new ByteArrayInputStream(marshaller.getXStream().toXML(config).getBytes()), outputStream);

                setConfig(config);

            } catch (IOException ex) {

                log.error("New configuration has not been persisted in store", ex);

            }

        }

    }

    @Override
    public boolean checkIfVersionAlreadyExists(Pair<Bundle, AppVersion> appConf) {

        for (Bundle bundle : getConfig().getBundles()) {

            if (bundle.getIdentifier().equals(appConf.getFirst().getIdentifier())
                    && bundle.getProfile().equals(appConf.getFirst().getProfile())) {

                for (AppVersion version : bundle.getVersions()) {
                    if (version.getNumber().equals(appConf.getSecond().getNumber())) {
                        return true;
                    }
                }

            }

        }

        return false;

    }

}
