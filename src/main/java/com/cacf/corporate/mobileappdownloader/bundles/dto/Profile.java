package com.cacf.corporate.mobileappdownloader.bundles.dto;

import java.util.Set;

/**
 * Created by jug on 22/10/2014.
 */
public class Profile {

    private String id;

    private String identifierSuffix;

    private Set<Application> applications;

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

    public Set<Application> getApplications() {
        return applications;
    }

    public void setApplications(Set<Application> applications) {
        this.applications = applications;
    }
}
