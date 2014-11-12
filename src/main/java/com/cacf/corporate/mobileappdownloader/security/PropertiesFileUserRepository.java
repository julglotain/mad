package com.cacf.corporate.mobileappdownloader.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.io.IOException;
import java.util.*;

/**
 * Created by jug on 21/10/2014.
 */
@Component
public class PropertiesFileUserRepository implements InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(PropertiesFileUserRepository.class);

    private final static String PREFIX = "mad.user.";
    private final static String PASSWORD_SUFFIX = "userpassword";
    private final static String BUNDLES_ACCESS_LIST_SUFFIX = "bundles";
    private final static String PROFILES_ACCESS_LIST_SUFFIX = "profiles";
    private final static String AUTHORITIES_LIST_SUFFIX = "authorities";

    @Inject
    @Qualifier("usersStore")
    private Resource propsFile;

    // holder for user data properties
    private Properties userDataProps = new Properties();

    private void refreshProperties() {

        try {
            userDataProps.load(propsFile.getInputStream());
        } catch (IOException e) {
            log.error("Error lors du chargement du fichier des utilisateurs", e);
        }

    }

    public List<User> getAll() {

        List<User> users = new ArrayList<>();

        for (String username : getAllUserNames()) {

            User user = new User();
            user.setUsername(username);
            user.setPassword(getUserPasswordByUsername(username));
            user.setBundles(getUserBundlesAccessList(username));
            user.setProfiles(getUserProfilesAccessList(username));

            users.add(user);

        }

        return users;
    }

    public List<String> getAllUserNames() {

        // get all user names
        String suffix = '.' + PROFILES_ACCESS_LIST_SUFFIX;
        List<String> usersName = new ArrayList<>();
        Enumeration<?> allKeys = userDataProps.propertyNames();
        int prefixLenght = PREFIX.length();
        int suffixLenght = suffix.length();
        while (allKeys.hasMoreElements()) {
            String key = (String) allKeys.nextElement();
            if (key.endsWith(suffix)) {
                String name = key.substring(prefixLenght);
                int endIndex = name.length() - suffixLenght;
                name = name.substring(0, endIndex);
                usersName.add(name);
            }
        }
        // Collections.sort(usersName);
        return usersName;

    }

    public User getUserByUsername(String username) {
        User user = new User();
        user.setUsername(username);
        String userPassword = getUserPasswordByUsername(username);
        // si pas de password associé c'est que l'utilisateur n'existe pas
        if (userPassword == null) {
            return null;
        }
        user.setPassword(getUserPasswordByUsername(username));
        user.setBundles(getUserBundlesAccessList(username));
        user.setProfiles(getUserProfilesAccessList(username));
        user.setAuthorities(getUserAuthorities(username));
        return user;
    }

    public String getUserPasswordByUsername(String username) {
        return getProperty(username, PASSWORD_SUFFIX);
    }

    private String getProperty(String username, String key) {
        return userDataProps.getProperty(PREFIX + username + '.' + key);
    }

    public Set<String> getUserBundlesAccessList(String username) {
        String bundlesFlatSet = getProperty(username, BUNDLES_ACCESS_LIST_SUFFIX);
        return new HashSet<>(Arrays.asList(bundlesFlatSet.split(",")));
    }

    public Set<String> getUserProfilesAccessList(String username) {
        String profilesFlatSet = getProperty(username, PROFILES_ACCESS_LIST_SUFFIX);
        return new HashSet<>(Arrays.asList(profilesFlatSet.split(",")));
    }

    private Set<GrantedAuthority> getUserAuthorities(String username) {
        String authoritiesFlatSet = getProperty(username, AUTHORITIES_LIST_SUFFIX);
        Set<GrantedAuthority> authorities = new HashSet<>();
        for(String auth : authoritiesFlatSet.split(",")){
            authorities.add(new GrantedAuthorityImpl(auth));
        }
        return authorities;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.refreshProperties();
    }
}
