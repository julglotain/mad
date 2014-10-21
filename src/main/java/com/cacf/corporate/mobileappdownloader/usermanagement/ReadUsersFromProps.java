package com.cacf.corporate.mobileappdownloader.usermanagement;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.*;

/**
 * Created by jug on 21/10/2014.
 */
public class ReadUsersFromProps {

    private final static String PREFIX = "mad.user.";
    private final static String PASSWORD_SUFFIX = "userpassword";
    private final static String BUNDLES_ACCESS_LIST_SUFFIX = "bundles";
    private final static String PROFILES_ACCESS_LIST_SUFFIX = "profiles";

    public static void main(String[] args) throws IOException {

        Resource res = new ClassPathResource("users.properties");

        Properties props = new Properties();

        props.load(res.getInputStream());

        for (String username : getAllUserNames(props)) {

            User user = new User();
            user.setUsername(username);
            user.setPassword(getUserPasswordByUsername(props, username));
            user.setBundlesAccessList(getUserBundlesAccessList(props, username));
            user.setProfilesAccessList(getUserProfilesAccessList(props, username));

            System.out.println(user);

        }


    }

    public static String[] getAllUserNames(Properties usersDataProps) {

        // get all user names
        String suffix = '.' + "profiles";
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
        return ulst.toArray(new String[0]);
    }

    public static String getUserPasswordByUsername(Properties usersDataProps, String username) {
        return getProperty(usersDataProps, username, PASSWORD_SUFFIX);
    }

    private static String getProperty(Properties usersDataProps, String username, String key) {
        return usersDataProps.getProperty(PREFIX + username + '.' + key);
    }

    public static Set<String> getUserBundlesAccessList(Properties usersDataProps, String username) {
        String bundlesFlatSet = getProperty(usersDataProps, username, BUNDLES_ACCESS_LIST_SUFFIX);
        return new HashSet<>(Arrays.asList(bundlesFlatSet.split(",")));
    }

    public static Set<String> getUserProfilesAccessList(Properties usersDataProps, String username) {
        String bundlesFlatSet = getProperty(usersDataProps, username, PROFILES_ACCESS_LIST_SUFFIX);
        return new HashSet<>(Arrays.asList(bundlesFlatSet.split(",")));
    }

}
