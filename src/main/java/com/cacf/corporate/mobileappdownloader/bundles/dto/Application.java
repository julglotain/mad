package com.cacf.corporate.mobileappdownloader.bundles.dto;

/**
 * Created by jug on 22/10/2014.
 */
public class Application {

    private String title;
    private String version;
    private String desc;
    private String dlManifestUrl;
    private String dlPreviewIconUrl;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDlManifestUrl() {
        return dlManifestUrl;
    }

    public void setDlManifestUrl(String dlManifestUrl) {
        this.dlManifestUrl = dlManifestUrl;
    }

    public String getDlPreviewIconUrl() {
        return dlPreviewIconUrl;
    }

    public void setDlPreviewIconUrl(String dlPreviewIconUrl) {
        this.dlPreviewIconUrl = dlPreviewIconUrl;
    }
}
