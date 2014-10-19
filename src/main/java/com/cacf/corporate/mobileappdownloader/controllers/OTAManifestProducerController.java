package com.cacf.corporate.mobileappdownloader.controllers;

import com.cacf.corporate.mobileappdownloader.services.ManifestGenerator;
import com.cacf.corporate.mobileappdownloader.services.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Created by cacf on 09/10/14.
 */
@Controller
public class OTAManifestProducerController {

    private static Logger log = LoggerFactory.getLogger(GetDownloadLinkController.class);

    @Inject
    private TokenService tokenService;

    @Inject
    private ManifestGenerator manifestGenerator;

    public static final String DOWNLOAD_MANIFEST_ROUTE_PATH = "/download/{token}/manifest.plist";


    @RequestMapping(value = DOWNLOAD_MANIFEST_ROUTE_PATH, method = RequestMethod.GET)
    public ResponseEntity<? extends Resource> download(@PathVariable("token") String token) throws InvalidTokenException, IOException {

        log.debug("Producing manifest.");

        boolean isTokenValid = tokenService.isValid(token);

        if (!isTokenValid) {

            throw new InvalidTokenException("Download token {" + token + "} is invalid.");

        }

        String manifestContent = manifestGenerator.generate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XML);
        headers.setContentDispositionFormData("attachment", "App.plist");

        return new ResponseEntity<>(new InputStreamResource(new ByteArrayInputStream(manifestContent.getBytes())), headers,
                HttpStatus.OK);

    }


    @ExceptionHandler(InvalidTokenException.class)
    public String invalidToken(InvalidTokenException ex) {

        log.error("Invalid token: ", ex);

        return "invalidToken";

    }

    @ExceptionHandler(Exception.class)
    public String exception(Exception e) {

        log.error("Exception ", e);

        return "somethingWentWrong";

    }

}
