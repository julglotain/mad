package com.cacf.corporate.mobileappdownloader.repositories;

/**
 * Created by cacf on 09/10/14.
 */
public class FailedToPersistTokenException extends Exception {

    public FailedToPersistTokenException(String message){
        super(message);
    }

    public FailedToPersistTokenException(String message, Throwable th){
        super(message,th);
    }

}
