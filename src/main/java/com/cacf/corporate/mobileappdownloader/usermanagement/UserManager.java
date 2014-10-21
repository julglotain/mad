package com.cacf.corporate.mobileappdownloader.usermanagement;

import java.util.List;

/**
 * Created by jug on 21/10/2014.
 */
public interface UserManager {

    User getByUsername(String username);

    List<User> getAll();

}
