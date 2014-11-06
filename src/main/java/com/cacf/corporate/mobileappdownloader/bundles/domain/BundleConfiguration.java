package com.cacf.corporate.mobileappdownloader.bundles.domain;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by jug on 20/10/2014.
 */
public class BundleConfiguration {

    private String identifier;

    private String name;


    private Set<ProfileConfiguration> profilesList;

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Bundle{");
        sb.append("identifier='").append(identifier).append('\'');
        sb.append(", profilesList=").append(profilesList);
        sb.append('}');
        return sb.toString();
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Set<ProfileConfiguration> getProfilesList() {
        if(profilesList==null){
            profilesList = new HashSet<>();
        }
        return profilesList;
    }

    public void setProfilesList(Set<ProfileConfiguration> profilesList) {
        if (profilesList == null) {
            profilesList = new HashSet<>();
        }
        this.profilesList = profilesList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProfileConfiguration findProfileById(String id) {

        for (ProfileConfiguration profConf : getProfilesList()) {
            if (profConf.getId().equals(id)) {
                return profConf;
            }
        }

        return null;
    }
}
