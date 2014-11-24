package com.cacf.corporate.mobileappdownloader.services;

/**
 * Created by jug on 24/11/2014.
 */
public class BundleAlreadyExistsException extends Exception {

    public BundleAlreadyExistsException(){
        super();
    }

    public BundleAlreadyExistsException(final String msg){
        super(msg);
    }

}
