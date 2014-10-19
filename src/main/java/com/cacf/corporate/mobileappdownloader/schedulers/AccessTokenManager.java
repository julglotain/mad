package com.cacf.corporate.mobileappdownloader.schedulers;

import com.cacf.corporate.mobileappdownloader.services.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Date;

/**
 * Created by cacf on 09/10/14.
 */
@Component
public class AccessTokenManager {

    private Logger log = LoggerFactory.getLogger(AccessTokenManager.class);

    @Inject
    private TokenService tokenService;

    @Scheduled(cron = "${mad.token.change.frequency}")
    public void change(){

        tokenService.createNewToken();

        log.debug("Access token changed at: {}", new Date().toString());

    }

}
