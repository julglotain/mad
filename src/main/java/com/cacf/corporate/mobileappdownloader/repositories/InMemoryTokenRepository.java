package com.cacf.corporate.mobileappdownloader.repositories;

import com.cacf.corporate.mobileappdownloader.dto.Token;
import org.springframework.stereotype.Component;

/**
 * Created by cacf on 09/10/14.
 */
@Component
public class InMemoryTokenRepository implements TokenRepository {

    private Token storedToken;

    @Override
    public Token getCurrent() {
        return storedToken;
    }

    @Override
    public void persist(Token token) throws FailedToPersistTokenException {

        this.storedToken = token;

    }
}
