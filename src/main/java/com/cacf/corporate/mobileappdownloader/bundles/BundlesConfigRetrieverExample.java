package com.cacf.corporate.mobileappdownloader.bundles;

import com.cacf.corporate.mobileappdownloader.bundles.domain.ApplicationConfiguration;
import com.cacf.corporate.mobileappdownloader.bundles.domain.Bundle;
import com.cacf.corporate.mobileappdownloader.bundles.domain.BundlesStoreConfiguration;
import com.cacf.corporate.mobileappdownloader.bundles.domain.Profile;
import com.cacf.corporate.mobileappdownloader.marshalling.XStreamXmlMarshaller;
import com.cacf.corporate.mobileappdownloader.marshalling.XmlMarshallerConfigurer;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by jug on 20/10/2014.
 */
public class BundlesConfigRetrieverExample {

    public static void main(String[] args) throws IOException {


        Set<XmlMarshallerConfigurer> xmlMarshallerConfigurers = new HashSet<>();
        xmlMarshallerConfigurers.add(new BundlesStoreConfigurationXmlMarshallerConfigurer());

        XStreamXmlMarshaller xmlMarshaller = new XStreamXmlMarshaller();

        for(XmlMarshallerConfigurer configurer : xmlMarshallerConfigurers){
            configurer.configure(xmlMarshaller);
        }

        BundlesStoreConfiguration config = (BundlesStoreConfiguration) xmlMarshaller.fromXML(new ClassPathResource("apps-bundle.xml").getFile());

        for(Bundle bundle : config.getBundlesList()){

            System.out.println(bundle.toString());

            for(Profile p : bundle.getProfilesList()){
                System.out.println("    " + p.toString());

                for(ApplicationConfiguration appConfig : p.getAppsConfigList()){
                    System.out.println("        " + appConfig.toString()  );
                }

            }

        }

    }

}
