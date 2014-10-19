package com.cacf.corporate.mobileappdownloader.utils;

import org.springframework.util.StringUtils;

/**
 * Created by jug on 17/10/2014.
 */
public class URLBuilder {

    private String scheme;
    private String serverName;
    private Integer serverPort;
    private String contextPath;
    private String path;


    public URLBuilder setScheme(String scheme) {
        this.scheme = scheme;
        return this;
    }

    public URLBuilder setServerName(String serverName) {
        this.serverName = serverName;
        return this;
    }

    public URLBuilder setServerPort(Integer serverPort) {
        this.serverPort = serverPort;
        return this;
    }

    public URLBuilder setContextPath(String contextPath) {
        this.contextPath = contextPath;
        return this;
    }

    public URLBuilder setPath(String path) {
        this.path = path;
        return this;
    }

    public String build(){

        StringBuilder sb = new StringBuilder();

        if(this.scheme!=null){
            sb.append(scheme).append("://");
        }

        if(this.serverName!=null){
            sb.append(serverName);
        }

        if(this.serverPort!=null){
            sb.append(":").append(serverPort);
        }

        if(!StringUtils.isEmpty(this.contextPath)){
            sb.append("/").append(this.contextPath);
        }

        if(this.path!=null){
            sb.append(this.path);
        }

        return sb.toString();

    }
}
