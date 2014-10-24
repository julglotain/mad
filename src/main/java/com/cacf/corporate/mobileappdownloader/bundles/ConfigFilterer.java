package com.cacf.corporate.mobileappdownloader.bundles;

import com.cacf.corporate.mobileappdownloader.bundles.domain.BundlesStoreConfiguration;
import com.cacf.corporate.mobileappdownloader.usermanagement.User;

/**
 * Created by jug on 21/10/2014.
 */
public interface ConfigFilterer {

    /**
     * Filter a bundles store configuration
     *
     * @param user
     * @return a filtered BundlesStoreConfiguration
     */
    BundlesStoreConfiguration filter(BundlesStoreConfiguration config, User user);

}
