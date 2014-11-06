package com.cacf.corporate.mobileappdownloader.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * Created by jug on 30/10/2014.
 */
@Component
public class AppsFilesManager {

    @Value("${mad.apps-bundle-store.uploaded.dir.path}")
    private String uploadAppsStoreBaseDir;

    /**
     * @param title
     * @param version
     * @param bundle
     * @param profileId
     * @return
     */
    public String createBundleDirectory(String title, String version, String bundle, String profileId) {

        String profilePath = (profileId != null) ? File.separator + profileId : "";

        return uploadAppsStoreBaseDir + File.separator + bundle.replace(".", File.separator) + profilePath + File.separator + version;

    }

}
