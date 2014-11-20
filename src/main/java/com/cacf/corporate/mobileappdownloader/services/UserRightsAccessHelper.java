package com.cacf.corporate.mobileappdownloader.services;

import com.cacf.corporate.mobileappdownloader.security.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Created by jug on 19/11/2014.
 */
@Component
public class UserRightsAccessHelper {

    private static final String ALL = "ALL";

    public boolean hasAccessTo(String bundleIdentifier, String profileId) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return hasBundleAccess(bundleIdentifier, user.getBundles()) && hasProfileAccess(profileId, user.getProfiles());
    }

    private boolean hasBundleAccess(String bundleIdentifier, Set<String> bundles) {

        if(bundles.contains(ALL)){
            return true;
        }

        for (String userBaseBundleName : bundles) {
            if (bundleIdentifier.startsWith(userBaseBundleName)) {
                return true;
            }
        }

        return false;
    }

    private boolean hasProfileAccess(String profileId, Set<String> profiles) {

        if(profiles.contains(ALL)){
            return true;
        }

        return profiles.contains(profileId);
    }

}
