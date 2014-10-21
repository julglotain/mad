package com.cacf.corporate.mobileappdownloader.usermanagement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.*;

/**
 * Created by jug on 21/10/2014.
 */
public class PropertiesFileUserRepository {

    private static final Logger log = LoggerFactory.getLogger(PropertiesFileUserRepository.class);

    private final static String PREFIX = "mad.user.";
    private final static String PASSWORD_SUFFIX = "userpassword";
    private final static String BUNDLES_ACCESS_LIST_SUFFIX = "bundles";
    private final static String PROFILES_ACCESS_LIST_SUFFIX = "profiles";

    private Resource propsFile;

    private Properties props;

    public PropertiesFileUserRepository(Resource res) {
        this.propsFile = res;
        props = new Properties();
        refreshProperties();
    }

    private void refreshProperties() {

        try {
            props.load(propsFile.getInputStream());
        } catch (IOException e) {
            log.error("Error lors du chargement du fichier des utilisateurs",e);
        }

    }

    public List<User> getAll(){

        List<User> users = new ArrayList<>();

        for (String username : getAllUserNames(props)) {

            User user = new User();
            user.setUsername(username);
            user.setPassword(getUserPasswordByUsername(props, username));
            user.setBundles(getUserBundlesAccessList(props, username));
            user.setProfiles(getUserProfilesAccessList(props, username));

            users.add(user);

        }

        return users;
    }

    public List<String> getAllUserNames(Properties usersDataProps) {

        // get all user names
        String suffix = '.' + PROFILES_ACCESS_LIST_SUFFIX;
        ArrayList<String> ulst = new ArrayList<String>();
        Enumeration<?> allKeys = usersDataProps.propertyNames();
        int prefixlen = PREFIX.length();
        int suffixlen = suffix.length();
        while (allKeys.hasMoreElements()) {
            String key = (String) allKeys.nextElement();
            if (key.endsWith(suffix)) {
                String name = key.substring(prefixlen);
                int endIndex = name.length() - suffixlen;
                name = name.substring(0, endIndex);
                ulst.add(name);
            }
        }

        Collections.sort(ulst);
        return ulst;
    }

    public User getUserByUsername(String username){
        User user = new User();
        user.setUsername(username);
        user.setPassword(getUserPasswordByUsername(props, username));
        user.setBundles(getUserBundlesAccessList(props, username));
        user.setProfiles(getUserProfilesAccessList(props, username));
        return user;
    }

    public String getUserPasswordByUsername(Properties usersDataProps, String username) {
        return getProperty(usersDataProps, username, PASSWORD_SUFFIX);
    }

    private String getProperty(Properties usersDataProps, String username, String key) {
        return usersDataProps.getProperty(PREFIX + username + '.' + key);
    }

    public Set<String> getUserBundlesAccessList(Properties usersDataProps, String username) {
        String bundlesFlatSet = getProperty(usersDataProps, username, BUNDLES_ACCESS_LIST_SUFFIX);
        return new HashSet<>(Arrays.asList(bundlesFlatSet.split(",")));
    }

    public Set<String> getUserProfilesAccessList(Properties usersDataProps, String username) {
        String bundlesFlatSet = getProperty(usersDataProps, username, PROFILES_ACCESS_LIST_SUFFIX);
        return new HashSet<>(Arrays.asList(bundlesFlatSet.split(",")));
    }

}
