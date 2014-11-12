package com.cacf.corporate.mobileappdownloader.dto.store;



import java.util.Set;

/**
 * Created by jug on 10/11/2014.
 */
public class Bundle {

    private String identifier;

    private String profile;

    private Set<AppVersion> versions;

    public Bundle() {
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public Set<AppVersion> getVersions() {
        return versions;
    }

    public void setVersions(Set<AppVersion> versions) {
        this.versions = versions;
    }
}
