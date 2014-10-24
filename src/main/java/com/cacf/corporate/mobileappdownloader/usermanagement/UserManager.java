package com.cacf.corporate.mobileappdownloader.usermanagement;

import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

/**
 * Created by jug on 21/10/2014.
 */
public interface UserManager extends UserDetailsService {

    User getByUsername(String username);

    List<User> getAll();

}
