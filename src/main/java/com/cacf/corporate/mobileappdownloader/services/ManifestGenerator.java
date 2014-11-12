package com.cacf.corporate.mobileappdownloader.services;

import com.cacf.corporate.mobileappdownloader.entities.store.AppVersion;
import com.cacf.corporate.mobileappdownloader.entities.store.Bundle;
import com.cacf.corporate.mobileappdownloader.utils.Pair;

/**
 * Created by cacf on 13/10/14.
 */
public interface ManifestGenerator {

    String generate();

    String generate( Pair<AppVersion, Bundle> appConfig);

}
