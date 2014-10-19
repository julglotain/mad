package com.cacf.corporate.mobileappdownloader.controllers;

/**
 * Created by cacf on 10/10/14.
 */
public class InvalidTokenException extends Exception {

    public InvalidTokenException(String message){
        super(message);
    }

}
