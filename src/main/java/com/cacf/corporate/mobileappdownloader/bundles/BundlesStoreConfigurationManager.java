package com.cacf.corporate.mobileappdownloader.bundles;

import com.cacf.corporate.mobileappdownloader.bundles.domain.ApplicationConfiguration;
import com.cacf.corporate.mobileappdownloader.bundles.domain.BundleConfiguration;
import com.cacf.corporate.mobileappdownloader.bundles.domain.BundlesStoreConfiguration;
import com.cacf.corporate.mobileappdownloader.bundles.domain.ProfileConfiguration;
import com.cacf.corporate.mobileappdownloader.usermanagement.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * Created by jug on 20/10/2014.
 * <p/>
 * TODO Gérer l'ordre des PROFILES retournés, donner un poids au profile et non un tri alphabetique
 */
@Component
public class BundlesStoreConfigurationManager {

    private static final Logger log = LoggerFactory.getLogger(BundlesStoreConfigurationManager.class);

    @Inject
    private ConfigFilterer filterDelegate;

    @Inject
    private BundleConfigurationRepository repository;

    public BundlesStoreConfiguration getConfig() {
        return this.repository.getConfig();
    }

    public void setConfig(BundlesStoreConfiguration config) {
        this.repository.setConfig(config);
    }

    public void persistConfig(BundlesStoreConfiguration config) {

        synchronized (getConfig()) {

            this.repository.persistConfig(config);

        }

    }

    public BundlesStoreConfiguration filterByUserAccessRights(User userInfo) {
        return filterDelegate.filter(getConfig(), userInfo);
    }


    public AppConfigurationTriplet findBy(String fullBundleIdentifier, String appTitle, String version) {

        BundlesStoreConfiguration config = getConfig();

        // deduction du profile à partir du full bundle identifier
        String bundleIdentifier = getBundleIdentifier(fullBundleIdentifier);
        String bundleIdentifierSuffix = getBundleIdentifierSuffix(fullBundleIdentifier);

        for (BundleConfiguration bundleConf : config.getBundlesList()) {

            if (bundleConf.getIdentifier().equals(bundleIdentifier) || bundleConf.getIdentifier().equals(fullBundleIdentifier)) {

                for (ProfileConfiguration profileConf : bundleConf.getProfilesList()) {

                    if ((profileConf.getIdentifierSuffix() != null && profileConf.getIdentifierSuffix().equals(bundleIdentifierSuffix))
                            || checkIfBundleWithNoIdentifierSuffix(fullBundleIdentifier, bundleConf.getIdentifier(), profileConf.getIdentifierSuffix())) {

                        for (ApplicationConfiguration appConf : profileConf.getAppsConfigList()) {

                            if (appConf.getTitle().equals(appTitle) && appConf.getVersion().equals(version)) {

                                return new AppConfigurationTriplet(bundleConf, profileConf, appConf);

                            }

                        }

                    }

                }

            }

        }

        return null;

    }

    private boolean checkIfBundleWithNoIdentifierSuffix(String fullBundleIdentifier, String bundleIdentifier, String identifierSuffix) {
        return fullBundleIdentifier.equals(bundleIdentifier) && identifierSuffix == null;
    }

    private String getBundleIdentifier(String bundleIdentifier) {
        return bundleIdentifier.substring(0, bundleIdentifier.lastIndexOf("."));
    }

    private String getBundleIdentifierSuffix(String bundleIdentifier) {
        return bundleIdentifier.substring(bundleIdentifier.lastIndexOf(".") + 1, bundleIdentifier.length());
    }

}
