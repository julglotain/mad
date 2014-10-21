package com.cacf.corporate.mobileappdownloader.bundles;

import com.cacf.corporate.mobileappdownloader.bundles.domain.BundleConfigurationRepository;
import com.cacf.corporate.mobileappdownloader.marshalling.XmlMarshaller;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.inject.Inject;

/**
 * Created by jug on 20/10/2014.
 */
public class BundlesStoreConfigurationManagerFactoryBean implements FactoryBean<BundlesStoreConfigurationManager> {

    @Inject
    private Environment env;

    @Inject
    private XmlMarshaller xmlMarshaller;

    private static String RES = "file:/Users/jug/Development/Pro/tmp/apps-bundle.xml";

    @Override
    public BundlesStoreConfigurationManager getObject() throws Exception {

        BundleConfigurationRepository repository = new BundleConfigurationRepository(xmlMarshaller, resolveStoreResource());

        BundlesStoreConfigurationManager manager =
                new BundlesStoreConfigurationManager(repository);

        // preload configuration
        manager.loadConfiguration();

        return manager;

    }

    private Resource resolveStoreResource() {
        return new PathMatchingResourcePatternResolver().getResource(RES);
    }

    @Override
    public Class<?> getObjectType() {
        return BundlesStoreConfigurationManager.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}