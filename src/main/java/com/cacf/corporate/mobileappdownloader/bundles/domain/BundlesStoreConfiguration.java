package com.cacf.corporate.mobileappdownloader.bundles.domain;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by jug on 20/10/2014.
 */
public class BundlesStoreConfiguration {

    private Set<BundleConfiguration> bundlesList;

    private String availableProfiles;

    public Set<BundleConfiguration> getBundlesList() {
        if (bundlesList == null) {
            bundlesList = new HashSet<>();
        }
        return bundlesList;
    }

    public void setBundlesList(Set<BundleConfiguration> bundlesList) {
        this.bundlesList = bundlesList;
    }

    public String getAvailableProfiles() {
        return availableProfiles;
    }

    public void setAvailableProfiles(String availableProfiles) {
        this.availableProfiles = availableProfiles;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("BundlesStoreConfiguration{");
        sb.append("bundlesList=").append(bundlesList);
        sb.append('}');
        return sb.toString();

    }

    public String[] getAvailableProfile() {
        return this.getAvailableProfiles().split(",");
    }

    public BundleConfiguration findBundleByIdentifier(String identifier) {

        for (BundleConfiguration bundle : getBundlesList()) {
            if (bundle.getIdentifier().equals(identifier)) {
                return bundle;
            }
        }

        return null;

    }

}
