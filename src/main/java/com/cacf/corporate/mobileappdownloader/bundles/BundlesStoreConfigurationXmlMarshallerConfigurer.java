package com.cacf.corporate.mobileappdownloader.bundles;

import com.cacf.corporate.mobileappdownloader.bundles.domain.ApplicationConfiguration;
import com.cacf.corporate.mobileappdownloader.bundles.domain.BundleConfiguration;
import com.cacf.corporate.mobileappdownloader.bundles.domain.BundlesStoreConfiguration;
import com.cacf.corporate.mobileappdownloader.bundles.domain.ProfileConfiguration;
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

        instance.registerAlias("bundle", BundleConfiguration.class);
        instance.useAttributeFor(BundleConfiguration.class, "identifier");
        instance.useAttributeFor(BundleConfiguration.class, "name");
        instance.addImplicitCollection(BundleConfiguration.class, "profilesList");

        instance.registerAlias("profile", ProfileConfiguration.class);
        instance.useAttributeFor(ProfileConfiguration.class, "id");
        instance.useAttributeFor(ProfileConfiguration.class, "identifierSuffix");
        instance.addImplicitCollection(ProfileConfiguration.class, "appsConfigList");

        instance.registerAlias("application", ApplicationConfiguration.class);
        instance.registerAlias("fileLocations",ApplicationConfiguration.FilesURILocations.class);

    }


}
