package com.cacf.corporate.mobileappdownloader.security;

import org.springframework.security.core.GrantedAuthority;

/**
 * Created by jug on 22/10/2014.
 */
public class GrantedAuthorityImpl implements GrantedAuthority {

    public static final GrantedAuthority ADMIN = new GrantedAuthorityImpl("ADMIN");
    public static final GrantedAuthority DOWNLOAD = new GrantedAuthorityImpl("DOWNLOAD");

    private String value;

    public GrantedAuthorityImpl(String val){
        this.value = val;
    }

    @Override
    public boolean equals(Object obj) {
        return this.value.equals(((GrantedAuthority) obj).getAuthority());
    }

    @Override
    public String getAuthority() {
        return value;
    }
}
