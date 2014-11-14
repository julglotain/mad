package com.cacf.corporate.mobileappdownloader.entities.store;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by jug on 06/11/2014.
 */
@XStreamAlias("appsStore")
public class AppsStore {

    @XStreamImplicit(itemFieldName = "bundle")
    private Set<Bundle> bundles;

    @XStreamAsAttribute
    private String availableProfiles;

    public Set<Bundle> getBundles() {
        if (bundles == null) {
            bundles = new HashSet<>();
        }
        return bundles;
    }

    public void setBundles(Set<Bundle> bundles) {
        this.bundles = bundles;
    }


    public Set<String> getAvailableProfiles() {

        Set<String> profiles = new HashSet<>();

        for (String prof : this.availableProfiles.split(",")) {
            profiles.add(prof);
        }

        return profiles;
    }

    public void setAvailableProfiles(String profiles) {
        this.availableProfiles = profiles;
    }
}
