package com.cacf.corporate.mobileappdownloader.services;

import com.cacf.corporate.mobileappdownloader.entities.store.AppVersion;
import com.cacf.corporate.mobileappdownloader.entities.store.AppsStore;
import com.cacf.corporate.mobileappdownloader.entities.store.Bundle;
import com.cacf.corporate.mobileappdownloader.repositories.AppsStoreRepository;
import com.cacf.corporate.mobileappdownloader.security.User;
import com.cacf.corporate.mobileappdownloader.utils.Pair;
import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Set;

/**
 * Created by jug on 06/11/2014.
 */
@Service
public class DefaultAppsStoreService implements AppsStoreService {


    private static final String ALL = "ALL";

    @Inject
    private AppsStoreRepository repository;

    @Override
    public AppsStore load() {
        return repository.getConfig();
    }

    @Override
    public AppsStore getUserRightsFilteredAppsStore(User usr) {

        AppsStore filteredAppsStore = new AppsStore();

        Set<Bundle> filteredBundles = filterByUserRightsAccess(usr);

        filteredAppsStore.setBundles(filteredBundles);

        return filteredAppsStore;
    }

    @Override
    public AppVersion addAppVersion() {
        return null;
    }



    @Override
    public AppVersion findAppVersion(String bundle, String profile, String versionNumber) throws AppVersionNotFoundException {

        Set<Bundle> allBundles = load().getBundles();

        for(Bundle bund : allBundles){

            if(bund.getIdentifier().equals(bundle) && bund.getProfile().equalsIgnoreCase(profile)){

                for(AppVersion appVersion : bund.getVersions()){

                    if(appVersion.equals(versionNumber)){
                        return appVersion;
                    }

                }

            }

        }

        throw new AppVersionNotFoundException(bundle, profile, versionNumber);

    }

    @Override
    public Pair<AppVersion, Bundle> findAppVersionWithBundle(String bundle, String profile, String versionNumber) throws AppVersionNotFoundException {

        Set<Bundle> allBundles = load().getBundles();

        for(Bundle bund : allBundles){

            if(bund.getIdentifier().equals(bundle) && bund.getProfile().equalsIgnoreCase(profile)){

                for(AppVersion appVersion : bund.getVersions()){

                    if(appVersion.getNumber().equals(versionNumber)){
                        return new Pair<>(appVersion,bund);
                    }

                }

            }

        }

        throw new AppVersionNotFoundException(bundle, profile, versionNumber);

    }

    private Set<Bundle> filterByUserRightsAccess(final User user) {

        Set<Bundle> currentConfig = load().getBundles();

        Set<Bundle> filteredBundles = (!user.getBundles().contains(ALL))
                ? filterBundlesByUserBundleBasenames(user, currentConfig) : currentConfig;

        return (!user.getProfiles().contains(ALL))
                ? filterBundlesByUserProfiles(user, filteredBundles) : filteredBundles;

    }

    private Set<Bundle> filterBundlesByUserProfiles(final User usr, Set<Bundle> bundles) {

        return Sets.filter(bundles, new Predicate<Bundle>() {

            @Override
            public boolean apply(Bundle input) {

                return usr.getProfiles().contains(input.getProfile());

            }

        });
    }

    private Set<Bundle> filterBundlesByUserBundleBasenames(final User usr, Set<Bundle> bundles) {

        return Sets.filter(bundles, new Predicate<Bundle>() {

            @Override
            public boolean apply(Bundle input) {

                for (String bundleBasename : usr.getBundles()) {
                    if (input.belongsToBaseBundle(bundleBasename)) {
                        return true;
                    }
                }

                return false;
            }

        });

    }
}
