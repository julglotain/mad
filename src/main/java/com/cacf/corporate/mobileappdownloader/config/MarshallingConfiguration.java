package com.cacf.corporate.mobileappdownloader.config;

import com.cacf.corporate.mobileappdownloader.bundles.BundlesStoreConfigurationXmlMarshallerConfigurer;
import com.cacf.corporate.mobileappdownloader.marshalling.XStreamXmlMarshaller;
import com.cacf.corporate.mobileappdownloader.marshalling.XmlMarshallerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by jug on 20/10/2014.
 */
@Configuration
public class MarshallingConfiguration {

    @Bean
    public XStreamXmlMarshaller xmlMarshaller() {

        XStreamXmlMarshaller xmlMarshaller = new XStreamXmlMarshaller();

        Set<XmlMarshallerConfigurer> xmlMarshallerConfigurers = new HashSet<>();
        xmlMarshallerConfigurers.add(new BundlesStoreConfigurationXmlMarshallerConfigurer());

        for (XmlMarshallerConfigurer configurer : xmlMarshallerConfigurers) {
            configurer.configure(xmlMarshaller);
        }

        return xmlMarshaller;
    }
}
