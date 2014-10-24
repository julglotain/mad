package com.cacf.corporate.mobileappdownloader.bundles;

import com.cacf.corporate.mobileappdownloader.bundles.domain.ApplicationConfiguration;
import com.cacf.corporate.mobileappdownloader.bundles.domain.BundleConfiguration;
import com.cacf.corporate.mobileappdownloader.bundles.domain.ProfileConfiguration;
import com.cacf.corporate.mobileappdownloader.utils.TripletHolder;

/**
 * Full set holder of information for an app config.
 *
 * Created by jug on 23/10/2014.
 *
 */
public class AppConfigurationTriplet implements TripletHolder<BundleConfiguration, ProfileConfiguration, ApplicationConfiguration> {

    private BundleConfiguration bundle;

    private ProfileConfiguration profile;

    private ApplicationConfiguration app;

    public AppConfigurationTriplet(BundleConfiguration _bundle, ProfileConfiguration _profile, ApplicationConfiguration _app){
        this.bundle = _bundle;
        this.profile = _profile;
        this.app = _app;
    }

    @Override
    public BundleConfiguration getFirst() {
        return bundle;
    }

    @Override
    public ProfileConfiguration getSecond() {
        return profile;
    }

    @Override
    public ApplicationConfiguration getThird() {
        return app;
    }
}
