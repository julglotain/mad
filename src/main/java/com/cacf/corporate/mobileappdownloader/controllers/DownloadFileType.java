package com.cacf.corporate.mobileappdownloader.controllers;

/**
 * Created by cacf on 13/10/14.
 */
public enum DownloadFileType {

    APP("APP"), SMALL_ICON("SMALL_ICON"), LARGE_ICON("LARGE_ICON");

    private String value;

    private DownloadFileType(String value){
        this.value = value;
    }

    public String getValue(){
        return this.value;
    }

}
