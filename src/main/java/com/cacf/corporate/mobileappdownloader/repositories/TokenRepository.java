package com.cacf.corporate.mobileappdownloader.repositories;

import com.cacf.corporate.mobileappdownloader.dto.Token;

/**
 * Created by cacf on 09/10/14.
 */
public interface TokenRepository {

    Token getCurrent();

    void persist(Token token) throws FailedToPersistTokenException;

}
