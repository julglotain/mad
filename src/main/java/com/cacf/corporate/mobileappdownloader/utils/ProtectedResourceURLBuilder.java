package com.cacf.corporate.mobileappdownloader.utils;

import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * Created by jug on 17/10/2014.
 */
public class ProtectedResourceURLBuilder {

    private String scheme;
    private String serverName;
    private Integer serverPort;
    private String contextPath;
    private String path;
    private String token;


    public ProtectedResourceURLBuilder setScheme(String scheme) {
        this.scheme = scheme;
        return this;
    }

    public ProtectedResourceURLBuilder setServerName(String serverName) {
        this.serverName = serverName;
        return this;
    }

    public ProtectedResourceURLBuilder setServerPort(Integer serverPort) {
        this.serverPort = serverPort;
        return this;
    }

    public ProtectedResourceURLBuilder setContextPath(String contextPath) {
        this.contextPath = contextPath;
        return this;
    }

    /**
     * Your path must contains a {token} char sequence.
     *
     * @param path
     * @return
     */
    public ProtectedResourceURLBuilder setPath(String path) {
        this.path = path;
        return this;
    }

    public ProtectedResourceURLBuilder withToken(String token) {
        this.token = token;
        return this;
    }

    public String build(Map<String, String> pathValues) {

        StringBuilder sb = new StringBuilder();

        if (this.scheme != null) {
            sb.append(scheme).append("://");
        }

        if (this.serverName != null) {
            sb.append(serverName);
        }

        if (this.serverPort != null) {
            sb.append(":").append(serverPort);
        }

        if (!StringUtils.isEmpty(this.contextPath)) {
            if (!this.contextPath.contains("/")) {
                sb.append("/");
            }
            sb.append(this.contextPath);
        }

        if (this.path != null) {
            sb.append(this.path);
        }

        String tmp = sb.toString().replace("{token}", this.token);

        for (Map.Entry<String, String> entry : pathValues.entrySet()) {
            tmp = tmp.replace("{" + entry.getKey() + "}", entry.getValue());
        }

        return tmp;

    }
}
