package com.cacf.corporate.mobileappdownloader.controllers;

/**
 * Created by cacf on 13/10/14.
 */
public class InvalidDownloadFileTypeException extends Exception {

    public InvalidDownloadFileTypeException(String message){
        super(message);
    }

}
