package com.cacf.corporate.mobileappdownloader.controllers;

import com.cacf.corporate.mobileappdownloader.download.InvalidTokenException;
import com.cacf.corporate.mobileappdownloader.entities.store.AppVersion;
import com.cacf.corporate.mobileappdownloader.entities.store.Bundle;
import com.cacf.corporate.mobileappdownloader.services.*;
import com.cacf.corporate.mobileappdownloader.utils.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Created by cacf on 09/10/14.
 */
@Controller
public class OTAManifestProducerController {

    private static Logger log = LoggerFactory.getLogger(AppsStoreController.class);

    public static final String DOWNLOAD_APP_MANIFEST_ROUTE_PATH =
            "/store/dl/{token}/bundle/{bundle}/profile/{profile}/version/{version}/manifest.plist";


    @Inject
    private TokenService tokenService;

    @Inject
    private ManifestGenerator manifestGenerator;

    @Inject
    private AppsStoreService appsStoreService;

    @Inject
    private UserRightsAccessHelper userRightsAccessHelper;

    @RequestMapping(value = DOWNLOAD_APP_MANIFEST_ROUTE_PATH, method = RequestMethod.GET)
    public ResponseEntity<? extends Resource>
    download(@PathVariable("token") String token,
             @PathVariable("bundle") String bundle,
             @PathVariable("profile") String profile, @PathVariable("version") String version)
            throws InvalidTokenException, IOException, AppVersionNotFoundException, AccessToProtectedResourceFailedException {

        log.debug("Producing manifest.");

        boolean isTokenValid = tokenService.isValid(token);

        if (!isTokenValid) {

            throw new InvalidTokenException("Download token '" + token + "' is invalid.");

        }

        if (!userRightsAccessHelper.hasAccessTo(bundle, profile)) {
            throw new AccessToProtectedResourceFailedException();
        }


        // recherche de la configuration de l'app pour laquelle on souhaite produire un manifest
        Pair<AppVersion, Bundle> appConfig = appsStoreService.findAppVersionWithBundle(bundle, profile, version);

        // generation du manifest
        String manifestContent = manifestGenerator.generate(appConfig);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XML);
        headers.setContentDispositionFormData("attachment", buildManifestFilename(appConfig));

        return new ResponseEntity<>(new InputStreamResource(new ByteArrayInputStream(manifestContent.getBytes())), headers,
                HttpStatus.OK);
    }


    private String buildManifestFilename(Pair<AppVersion, Bundle> appConfig) {
        return appConfig.getFirst().getName().replace(" ", "_") + "-" + appConfig.getFirst().getNumber() + ".plist";
    }

}
