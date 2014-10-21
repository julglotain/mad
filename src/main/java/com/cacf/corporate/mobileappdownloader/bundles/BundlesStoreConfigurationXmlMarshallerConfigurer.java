package com.cacf.corporate.mobileappdownloader.bundles;

import com.cacf.corporate.mobileappdownloader.bundles.domain.ApplicationConfiguration;
import com.cacf.corporate.mobileappdownloader.bundles.domain.Bundle;
import com.cacf.corporate.mobileappdownloader.bundles.domain.BundlesStoreConfiguration;
import com.cacf.corporate.mobileappdownloader.bundles.domain.Profile;
import com.cacf.corporate.mobileappdownloader.marshalling.XmlMarshaller;
import com.cacf.corporate.mobileappdownloader.marshalling.XmlMarshallerConfigurer;

/**
 * Created by jug on 20/10/2014.
 */
public class BundlesStoreConfigurationXmlMarshallerConfigurer implements XmlMarshallerConfigurer {

    @Override
    public void configure(XmlMarshaller instance) {

        instance.registerAlias("bundles", BundlesStoreConfiguration.class);
        instance.addImplicitCollection(BundlesStoreConfiguration.class, "bundlesList");

        instance.registerAlias("bundle", Bundle.class);
        instance.useAttributeFor(Bundle.class, "identifier");
        instance.addImplicitCollection(Bundle.class, "profilesList");

        instance.registerAlias("profile", Profile.class);
        instance.useAttributeFor(Profile.class, "id");
        instance.useAttributeFor(Profile.class, "identifierSuffix");
        instance.addImplicitCollection(Profile.class, "appsConfigList");

        instance.registerAlias("application", ApplicationConfiguration.class);

    }


}
