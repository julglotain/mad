package com.cacf.corporate.mobileappdownloader.dto;

import java.util.Comparator;

/**
 * Created by jug on 31/10/2014.
 */
public class BundleProfileCouple implements Comparable<BundleProfileCouple>{

    String bundle;

    String profile;

    public BundleProfileCouple(){}

    public BundleProfileCouple(String bundle, String profile) {
        this.bundle = bundle;
        this.profile = profile;
    }

    public String getBundle() {
        return bundle;
    }

    public String getProfile() {
        return profile;
    }

    @Override
    public int compareTo(BundleProfileCouple o) {
        return this.getBundle().compareTo(o.getBundle());
    }
}
