package com.cacf.corporate.mobileappdownloader.bundles.dto;

import java.util.Set;

/**
 * Created by jug on 22/10/2014.
 */
public class Bundle {

    private String identifier;

    private String name;

    private Set<Profile> profiles;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Set<Profile> getProfiles() {
        return profiles;
    }

    public void setProfiles(Set<Profile> profiles) {
        this.profiles = profiles;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
