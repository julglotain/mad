package com.cacf.corporate.mobileappdownloader.repositories;

import com.cacf.corporate.mobileappdownloader.entities.store.AppsStore;

/**
 * Created by jug on 06/11/2014.
 */
public interface AppsStoreRepository {

    AppsStore getConfig();

    void forceReloading();

    void setConfig(AppsStore config);

    void persistConfig(AppsStore config);

}
