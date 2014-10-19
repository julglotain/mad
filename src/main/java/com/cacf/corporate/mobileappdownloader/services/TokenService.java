package com.cacf.corporate.mobileappdownloader.services;

import com.cacf.corporate.mobileappdownloader.dto.Token;
import com.cacf.corporate.mobileappdownloader.repositories.FailedToPersistTokenException;
import com.cacf.corporate.mobileappdownloader.repositories.TokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.inject.Inject;
import java.util.UUID;

/**
 * Created by cacf on 09/10/14.
 */
@Service
public class TokenService {

    @Inject
    private TokenRepository tokenRepository;

    public synchronized Token getCurrentToken(){

        Token currentToken = tokenRepository.getCurrent();

        if(currentToken==null){
            currentToken = createNewToken();
        }

        return currentToken;
    }

    public Token createNewToken() {

        Token newToken = new Token();

        newToken.setValue(generateRandomToken());

        try {
            tokenRepository.persist(newToken);
        } catch (FailedToPersistTokenException e) {
            e.printStackTrace();
        }

        return newToken;
    }

    public boolean isValid(String tokenValue){

        if(StringUtils.isEmpty(tokenValue)){
            return false;
        }

        return getCurrentToken().getValue().equals(tokenValue);

    }

    private String generateRandomToken() {
        return UUID.randomUUID().toString();
    }


}
