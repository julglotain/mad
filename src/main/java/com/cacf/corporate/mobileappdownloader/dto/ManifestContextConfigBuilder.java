package com.cacf.corporate.mobileappdownloader.dto;

public class ManifestContextConfigBuilder {
    private String appBundleIdentifier;
    private String appTitle;
    private String proxyDlAppUrl;
    private String proxyDlSmallIconUrl;
    private String proxyDlLargeIconUrl;
    private String token;

    public ManifestContextConfigBuilder withAppBundleIdentifier(String appBundleIdentifier) {
        this.appBundleIdentifier = appBundleIdentifier;
        return this;
    }

    public ManifestContextConfigBuilder withAppTitle(String appTitle) {
        this.appTitle = appTitle;
        return this;
    }

    public ManifestContextConfigBuilder withProxyDlAppUrl(String proxyDlAppUrl) {
        this.proxyDlAppUrl = proxyDlAppUrl;
        return this;
    }

    public ManifestContextConfigBuilder withProxyDlSmallIconUrl(String proxyDlSmallIconUrl) {
        this.proxyDlSmallIconUrl = proxyDlSmallIconUrl;
        return this;
    }

    public ManifestContextConfigBuilder withProxyDlLargeIconUrl(String proxyDlLargeIconUrl) {
        this.proxyDlLargeIconUrl = proxyDlLargeIconUrl;
        return this;
    }

    public ManifestContextConfigBuilder withToken(String token) {
        this.token = token;
        return this;
    }

    public ManifestContextConfig createManifestContextConfig() {
        return new ManifestContextConfig(appBundleIdentifier, appTitle, proxyDlAppUrl, proxyDlSmallIconUrl, proxyDlLargeIconUrl, token);
    }
}