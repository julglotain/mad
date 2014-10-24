package com.cacf.corporate.mobileappdownloader.bundles;

import com.cacf.corporate.mobileappdownloader.bundles.domain.BundleConfiguration;
import com.cacf.corporate.mobileappdownloader.bundles.domain.BundlesStoreConfiguration;
import com.cacf.corporate.mobileappdownloader.bundles.domain.ProfileConfiguration;
import com.cacf.corporate.mobileappdownloader.usermanagement.User;
import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Created by jug on 21/10/2014.
 */
@Component
public class PropertiesBasedUserRightsAccessFilter implements ConfigFilterer {

    private static final String ALL = "ALL";

    public BundlesStoreConfiguration filter(BundlesStoreConfiguration toFilter, User userInfo) {

        BundlesStoreConfiguration result = new BundlesStoreConfiguration();

        Predicate userAllowedBundlesPredicate = createAllowedBundlesPredicate(userInfo.getBundles());

        Set<BundleConfiguration> filteredBundles =
                userInfo.getBundles().contains(ALL) ?
                    toFilter.getBundlesList() : Sets.filter(toFilter.getBundlesList(), userAllowedBundlesPredicate);

        result.setBundlesList(filteredBundles);

        for (BundleConfiguration allowedBundle : result.getBundlesList()) {

            Predicate<ProfileConfiguration> userAllowedBundleProfilesPredicate = createAllowedBundleProfilesPredicate(userInfo.getProfiles());

            Set<ProfileConfiguration> filteredProfiles =
                    userInfo.getProfiles().contains(ALL) ?
                            allowedBundle.getProfilesList() : Sets.filter(allowedBundle.getProfilesList(), userAllowedBundleProfilesPredicate);

            allowedBundle.setProfilesList(filteredProfiles);

        }

        return result;
    }

    private Predicate<BundleConfiguration> createAllowedBundlesPredicate(final Set<String> allowedBundlesIdentifier) {

        return new Predicate<BundleConfiguration>() {

            @Override
            public boolean apply(BundleConfiguration bundle) {

                return allowedBundlesIdentifier.contains(bundle.getIdentifier());

            }

        };

    }

    private Predicate<ProfileConfiguration> createAllowedBundleProfilesPredicate(final Set<String> allowedProfilesIDs) {

        return new Predicate<ProfileConfiguration>() {

            @Override
            public boolean apply(ProfileConfiguration profile) {

                return allowedProfilesIDs.contains(profile.getId());

            }

        };

    }

}
