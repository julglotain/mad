package com.cacf.corporate.mobileappdownloader.services;

/**
 * Created by jug on 10/11/2014.
 */
public class AppVersionNotFoundException extends Exception {

    private static final String APP_DOES_NOT_EXIST_MSG = "The following app version doesn't exist: [Bundle= %s, Profile= %s, Version= %s]";

    public AppVersionNotFoundException() {
        super();
    }

    public AppVersionNotFoundException(String msg) {
        super(msg);
    }

    public AppVersionNotFoundException(String bundle, String profile, String versionNumber) {

        super(String.format(APP_DOES_NOT_EXIST_MSG, bundle, profile, versionNumber));

    }


}
