package com.cacf.corporate.mobileappdownloader.dto.store;

/**
 * Created by jug on 10/11/2014.
 */
public class AppVersion {

    private String name;

    private String number;

    private String description;

    private String manifestDownloadUrl;

    private String iconDownloadUrl;


    public AppVersion() {
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getManifestDownloadUrl() {
        return manifestDownloadUrl;
    }

    public void setManifestDownloadUrl(String manifestDownloadUrl) {
        this.manifestDownloadUrl = manifestDownloadUrl;
    }

    public String getIconDownloadUrl() {
        return iconDownloadUrl;
    }

    public void setIconDownloadUrl(String iconDownloadUrl) {
        this.iconDownloadUrl = iconDownloadUrl;
    }
}
