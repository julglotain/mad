package com.cacf.corporate.mobileappdownloader.bundles.domain;

import com.cacf.corporate.mobileappdownloader.marshalling.XmlMarshaller;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by jug on 20/10/2014.
 */
public class BundleConfigurationRepository {

    private static final Logger log = LoggerFactory.getLogger(BundleConfigurationRepository.class);

    private XmlMarshaller marshaller;

    private Resource storeSource;

    private BundlesStoreConfiguration storeConfigurationHolder;

    public BundleConfigurationRepository(XmlMarshaller xmlMarshaller, Resource res){
        this.marshaller = xmlMarshaller;
        this.storeSource = res;
    }

    public void loadConfiguration() throws IOException {
        this.storeConfigurationHolder = (BundlesStoreConfiguration) marshaller.fromXML(storeSource.getFile());
    }

    public BundlesStoreConfiguration getConfig() {
        return storeConfigurationHolder;
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

                log.error("New configuration has not been persisted in store",ex);

            }

        }

    }

}
