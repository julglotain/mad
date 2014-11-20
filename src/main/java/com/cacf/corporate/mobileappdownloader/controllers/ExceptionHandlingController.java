package com.cacf.corporate.mobileappdownloader.controllers;

import com.cacf.corporate.mobileappdownloader.download.InvalidTokenException;
import com.cacf.corporate.mobileappdownloader.services.AccessToProtectedResourceFailedException;
import com.cacf.corporate.mobileappdownloader.services.AppVersionAlreadyExistsException;
import com.cacf.corporate.mobileappdownloader.services.AppVersionNotFoundException;
import com.cacf.corporate.mobileappdownloader.services.FileWritingFailureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jug on 19/11/2014.
 */
@ControllerAdvice
public class ExceptionHandlingController {

    private static Logger log = LoggerFactory.getLogger(ExceptionHandlingController.class);

    @ExceptionHandler(AppVersionNotFoundException.class)
    public String applicationNotFoundHandler(AppVersionNotFoundException ex) {

        log.error(ex.getMessage());

        return "applicationNotFound";

    }


    @ExceptionHandler(InvalidTokenException.class)
    public String invalidToken(InvalidTokenException ex) {

        log.error(ex.getMessage());

        return "invalidToken";

    }

    @ExceptionHandler(Exception.class)
    public String exception(Exception e) {

        log.debug("Base Exception ", e);

        return "somethingWentWrong";

    }

    @ExceptionHandler(AppVersionAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> appVersionAlreadyExistsException(AppVersionAlreadyExistsException ex) {

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("result", "KO");
        responseBody.put("message", "App has not been added to the store, because there is already a similar app version on the store.");
        return new ResponseEntity<>(responseBody, HttpStatus.NOT_ACCEPTABLE);

    }

    @ExceptionHandler(FileWritingFailureException.class)
    public ResponseEntity<Map<String, Object>> fileWritingFailureException(FileWritingFailureException ex) {

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("result", "KO");
        responseBody.put("message", "App has not been added to the store, an error occured when attempting to write app files.");
        return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @ExceptionHandler(AccessToProtectedResourceFailedException.class)
    public ResponseEntity<Map<String, Object>> accessToProtectedResourceException(AccessToProtectedResourceFailedException ex) {

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("result", "KO");
        responseBody.put("message", "Access to this resource is forbidden to this user.");
        return new ResponseEntity<>(responseBody, HttpStatus.FORBIDDEN);

    }


}
