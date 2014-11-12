package com.cacf.corporate.mobileappdownloader.config;

import com.cacf.corporate.mobileappdownloader.entities.store.AppVersion;
import com.cacf.corporate.mobileappdownloader.entities.store.AppsStore;
import com.cacf.corporate.mobileappdownloader.entities.store.Bundle;
import com.thoughtworks.xstream.XStream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.xstream.XStreamMarshaller;

/**
 * Created by jug on 20/10/2014.
 */
@Configuration
public class MarshallingConfiguration {

    @Bean
    public XStreamMarshaller marshaller() {

        XStreamMarshaller marshaller = new XStreamMarshaller();

        marshaller.setAnnotatedClasses(AppsStore.class, Bundle.class, AppVersion.class);

        return marshaller;
    }


}
