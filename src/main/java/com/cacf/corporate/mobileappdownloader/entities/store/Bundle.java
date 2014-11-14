package com.cacf.corporate.mobileappdownloader.entities.store;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by jug on 06/11/2014.
 */
@XStreamAlias("bundle")
public class Bundle {

    // bundle identifier
    @XStreamAsAttribute
    private String identifier;

    // profile associated to this bundle
    @XStreamAsAttribute
    private String profile;

    @XStreamImplicit(itemFieldName = "version")
    private Set<AppVersion> versions;

    public Bundle() {
    }

    public Bundle(String identifier) {
        this.identifier = identifier;
    }

    public Bundle(String identifier, String profile) {
        this.identifier = identifier;
        this.profile = profile;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public Set<AppVersion> getVersions() {
        if (versions == null) {
            versions = new HashSet<>();
        }
        return versions;
    }

    public void setVersions(Set<AppVersion> versions) {
        this.versions = versions;
    }

    public boolean belongsToBaseBundle(String baseBundle) {
        if (StringUtils.isEmpty(identifier)) return false;
        return identifier.startsWith(baseBundle);
    }

}
