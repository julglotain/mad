package com.cacf.corporate.mobileappdownloader.usermanagement;

import java.util.Set;

/**
 * Created by jug on 21/10/2014.
 */
public class User {

    private String username;
    private String password;
    private Set<String> bundles;
    private Set<String> profiles;

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
        return bundles;
    }

    public void setBundles(Set<String> bundles) {
        this.bundles = bundles;
    }

    public Set<String> getProfiles() {
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
}
