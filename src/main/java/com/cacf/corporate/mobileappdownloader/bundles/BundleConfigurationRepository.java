package com.cacf.corporate.mobileappdownloader.bundles;

import com.cacf.corporate.mobileappdownloader.bundles.domain.BundlesStoreConfiguration;
import com.cacf.corporate.mobileappdownloader.marshalling.XmlMarshaller;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by jug on 20/10/2014.
 */
@Component
public class BundleConfigurationRepository {

    private static final Logger log = LoggerFactory.getLogger(BundleConfigurationRepository.class);

    // holder for bundle configuration
    private BundlesStoreConfiguration storeConfigurationHolder;

    @Inject
    private XmlMarshaller marshaller;

    @Inject
    @Qualifier("bundlesAppsStore")
    private Resource storeSource;

    private void loadConfiguration() throws IOException {
        log.debug("Load apps store configuration from {}",storeSource.getFile().getAbsolutePath());
        this.storeConfigurationHolder = (BundlesStoreConfiguration) marshaller.fromXML(storeSource.getFile());
    }

    public BundlesStoreConfiguration getConfig() {
        if(storeConfigurationHolder==null){
            try {
                loadConfiguration();
            } catch (IOException e) {
                log.error("Erreur lors de la récupération de la configuration des bundles d'apps.",e);
            }
        }
        return storeConfigurationHolder;
    }

    public void forceReloading(){
        try {
            loadConfiguration();
        } catch (IOException e) {
            log.error("Erreur lors de la récupération de la configuration des bundles d'apps.",e);
        }
    }

    public void setConfig(BundlesStoreConfiguration config) {
        this.storeConfigurationHolder = config;
    }

    public synchronized void persistConfig(BundlesStoreConfiguration config) {

        synchronized (getConfig()) {

            try (FileOutputStream outputStream = new FileOutputStream(storeSource.getFile())) {

                IOUtils.copy(new ByteArrayInputStream(marshaller.toXML(config).getBytes()), outputStream);

                setConfig(config);

            } catch (IOException ex) {

                log.error("New configuration has not been persisted in store", ex);

            }

        }

    }

}
