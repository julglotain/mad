package com.cacf.corporate.mobileappdownloader.dto;

/**
 * Created by cacf on 13/10/14.
 */
public class ManifestContextConfig {

    private String appBundleIdentifier;

    private String appTitle;

    private String proxyDlAppUrl;

    private String proxyDlSmallIconUrl;

    private String proxyDlLargeIconUrl;

    public ManifestContextConfig(String appBundleIdentifier, String appTitle, String proxyDlAppUrl, String proxyDlSmallIconUrl, String proxyDlLargeIconUrl) {
        this.appBundleIdentifier = appBundleIdentifier;
        this.appTitle = appTitle;
        this.proxyDlAppUrl = proxyDlAppUrl;
        this.proxyDlSmallIconUrl = proxyDlSmallIconUrl;
        this.proxyDlLargeIconUrl = proxyDlLargeIconUrl;
    }

    public String getAppBundleIdentifier() {
        return appBundleIdentifier;
    }

    public String getAppTitle() {
        return appTitle;
    }

    public String getProxyDlAppUrl() {
        return proxyDlAppUrl;
    }

    public String getProxyDlSmallIconUrl() {
        return proxyDlSmallIconUrl;
    }

    public String getProxyDlLargeIconUrl() {
        return proxyDlLargeIconUrl;
    }
}
