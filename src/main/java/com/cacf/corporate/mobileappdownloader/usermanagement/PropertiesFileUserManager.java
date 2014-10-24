package com.cacf.corporate.mobileappdownloader.usermanagement;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by jug on 21/10/2014.
 */
@Component
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.getUserByUsername(username);

        if(user == null){
            throw new UsernameNotFoundException("L'utilisateur '" + username + "' n'existe pas.");
        }

        return user;
    }
}
