package com.cacf.corporate.mobileappdownloader.services;

import com.cacf.corporate.mobileappdownloader.bundles.AppConfigurationTriplet;

/**
 * Created by cacf on 13/10/14.
 */
public interface ManifestGenerator {

    String generate();

    String generate(AppConfigurationTriplet appConfig);

}
