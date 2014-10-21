package com.cacf.corporate.mobileappdownloader.usermanagement;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by jug on 21/10/2014.
 */
public class PropertiesFileUserManager implements UserManager {

    @Inject
    private PropertiesFileUserRepository userRepository;

    @Override
    public User getByUsername(String username) {
        return userRepository.getUserByUsername(username);
    }

    @Override
    public List<User> getAll() {
        return userRepository.getAll();
    }
}
