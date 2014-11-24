package com.cacf.corporate.mobileappdownloader.repositories;

import com.cacf.corporate.mobileappdownloader.entities.store.AppVersion;
import com.cacf.corporate.mobileappdownloader.entities.store.AppsStore;
import com.cacf.corporate.mobileappdownloader.entities.store.Bundle;
import com.cacf.corporate.mobileappdownloader.utils.Pair;

/**
 * Created by jug on 06/11/2014.
 */
public interface AppsStoreRepository {

    AppsStore getConfig();

    void forceReloading();

    void setConfig(AppsStore config);

    void persistConfig(AppsStore config);

    boolean checkIfVersionAlreadyExists(Pair<Bundle, AppVersion> appConf);

    boolean checkIfBundleAlreadyExists(String identifier, String profile);

}
