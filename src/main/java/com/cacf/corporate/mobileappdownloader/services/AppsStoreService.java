package com.cacf.corporate.mobileappdownloader.services;

import com.cacf.corporate.mobileappdownloader.entities.store.AppVersion;
import com.cacf.corporate.mobileappdownloader.entities.store.AppsStore;
import com.cacf.corporate.mobileappdownloader.entities.store.Bundle;
import com.cacf.corporate.mobileappdownloader.security.User;
import com.cacf.corporate.mobileappdownloader.utils.Pair;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Part;
import java.util.Set;

/**
 * Created by jug on 06/11/2014.
 */
public interface AppsStoreService {

    /**
     * Retourne l'ensemble de la configuration de l'apps store.
     *
     * @return
     */
    AppsStore load();

    /**
     * Retourne les différents bundles et apps accessibles pour un utilisateur donné.
     *
     * @param user le user en question
     * @return un AppsStore filtré selon les droits de l'utilisateur
     */
    AppsStore getUserRightsFilteredAppsStore(User user);

    AppVersion addAppVersion(Pair<Bundle, AppVersion> appConf, MultipartFile app, MultipartFile smallIcon, MultipartFile largeIcon) throws AppVersionAlreadyExistsException, FileWritingFailureException;

    AppVersion findAppVersion(String bundle, String profile, String versionNumber) throws AppVersionNotFoundException;

    Pair<AppVersion, Bundle> findAppVersionWithBundle(String bundle, String profile, String versionNumber) throws AppVersionNotFoundException;

    Set<String> getAvailableProfiles();

    Set<Bundle> findBundlesByIdentifierPrefix(String idPrefix);

    void removeBundle(String identifier, String profile);

    void removeApp(String bundleIdentifier, String profile, String versionNumber);

}
