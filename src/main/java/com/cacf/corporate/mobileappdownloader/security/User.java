package com.cacf.corporate.mobileappdownloader.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by jug on 21/10/2014.
 */
public class User implements UserDetails {

    private String username;
    private String password;
    private Set<String> bundles;
    private Set<String> profiles;
    private Set<GrantedAuthority> authorities;

    public void setAuthorities(Set<GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getBundles() {
        if(bundles==null){
            bundles = new HashSet<>();
        }
        return bundles;
    }

    public void setBundles(Set<String> bundles) {
        this.bundles = bundles;
    }

    public Set<String> getProfiles() {
        if(profiles==null){
            profiles = new HashSet<>();
        }
        return profiles;
    }

    public void setProfiles(Set<String> profiles) {
        this.profiles = profiles;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("User{");
        sb.append("username='").append(username).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", bundles=").append(bundles);
        sb.append(", profiles=").append(profiles);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
