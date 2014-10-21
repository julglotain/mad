package com.cacf.corporate.mobileappdownloader.bundles.domain;

import java.util.Set;

/**
 * Created by jug on 20/10/2014.
 */
public class Bundle {

    private String identifier;

    private Set<Profile> profilesList;

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Bundle{");
        sb.append("identifier='").append(identifier).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Set<Profile> getProfilesList() {
        return profilesList;
    }

    public void setProfilesList(Set<Profile> profilesList) {
        this.profilesList = profilesList;
    }
}
