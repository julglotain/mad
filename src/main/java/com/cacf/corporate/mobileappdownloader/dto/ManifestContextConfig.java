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

    private String token;

    private static final String TOKENIZED_TOKEN_PLACEHOLDER = "{token}";

    public ManifestContextConfig(String appBundleIdentifier, String appTitle, String proxyDlAppUrl, String proxyDlSmallIconUrl, String proxyDlLargeIconUrl, String token) {
        this.appBundleIdentifier = appBundleIdentifier;
        this.appTitle = appTitle;
        this.proxyDlAppUrl = proxyDlAppUrl;
        this.proxyDlSmallIconUrl = proxyDlSmallIconUrl;
        this.proxyDlLargeIconUrl = proxyDlLargeIconUrl;
        this.token = token;
    }

    public String getAppBundleIdentifier() {
        return appBundleIdentifier;

    }

    public String getAppTitle() {
        return appTitle;
    }

    public String getProxyDlAppUrl() {
        return proxyDlAppUrl.replace(TOKENIZED_TOKEN_PLACEHOLDER,token);
    }

    public String getProxyDlSmallIconUrl() {
        return proxyDlSmallIconUrl.replace(TOKENIZED_TOKEN_PLACEHOLDER,token);
    }

    public String getProxyDlLargeIconUrl() {
        return proxyDlLargeIconUrl.replace(TOKENIZED_TOKEN_PLACEHOLDER,token);
    }

    public String getToken() {
        return token;
    }
}
