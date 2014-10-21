package com.cacf.corporate.mobileappdownloader.bundles.domain;

import java.util.Set;

/**
 * Created by jug on 20/10/2014.
 */
public class Profile {

    private String id;

    private String identifierSuffix;

    private Set<ApplicationConfiguration> appsConfigList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdentifierSuffix() {
        return identifierSuffix;
    }

    public void setIdentifierSuffix(String identifierSuffix) {
        this.identifierSuffix = identifierSuffix;
    }

    public Set<ApplicationConfiguration> getAppsConfigList() {
        return appsConfigList;
    }

    public void setAppsConfigList(Set<ApplicationConfiguration> appsConfigList) {
        this.appsConfigList = appsConfigList;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Profile{");
        sb.append("id='").append(id).append('\'');
        sb.append(", identifierSuffix='").append(identifierSuffix).append('\'');
        sb.append('}');
        return sb.toString();
    }

}