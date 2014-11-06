package com.cacf.corporate.mobileappdownloader.download;

import com.cacf.corporate.mobileappdownloader.bundles.AppConfigurationTriplet;
import com.cacf.corporate.mobileappdownloader.bundles.BundlesStoreConfigurationManager;
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
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLDecoder;

/**
 * Created by cacf on 09/10/14.
 */
@Controller
public class OTAManifestProducerController {

    private static Logger log = LoggerFactory.getLogger(GetDownloadLinkController.class);

    public static final String DOWNLOAD_APP_MANIFEST_ROUTE_PATH =
            "/download/{token}/bundle/{bundle}/app/{app}/version/{version}/manifest.plist";


    @Inject
    private TokenService tokenService;

    @Inject
    private ManifestGenerator manifestGenerator;

    @Inject
    private BundlesStoreConfigurationManager bundlesStoreConfigurationManager;


    @RequestMapping(value = DOWNLOAD_APP_MANIFEST_ROUTE_PATH, method = RequestMethod.GET)
    public ResponseEntity<? extends Resource>
    download(@PathVariable("token") String token,
             @PathVariable("bundle") String bundle,
             @PathVariable("app") String app, @PathVariable("version") String version)
            throws InvalidTokenException, IOException, ApplicationConfigurationNotFoundException {

        log.debug("Producing manifest.");

        boolean isTokenValid = tokenService.isValid(token);

        if (!isTokenValid) {

            throw new InvalidTokenException("Download token '" + token + "' is invalid.");

        }

        // recherche de la configuration de l'app pour laquelle on souhaite produire un manifest
        AppConfigurationTriplet appConfig = bundlesStoreConfigurationManager.findBy(bundle, URLDecoder.decode(app,"utf-8"), version);

        if (appConfig == null) {

            throw new ApplicationConfigurationNotFoundException("The application was not found, no way to produce a manifest.");

        }

        // generation du manifest

        String manifestContent = manifestGenerator.generate(appConfig);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XML);
        headers.setContentDispositionFormData("attachment", buildManifestFilename(appConfig));

        return new ResponseEntity<>(new InputStreamResource(new ByteArrayInputStream(manifestContent.getBytes())), headers,
                HttpStatus.OK);

    }

    private String buildManifestFilename(AppConfigurationTriplet appConfig){
        return appConfig.getThird().getTitle().replace(" ","_") + "-" + appConfig.getThird().getVersion() + ".plist";
    }


    @ExceptionHandler(ApplicationConfigurationNotFoundException.class)
    public String applicationNotFoundHandler(ApplicationConfigurationNotFoundException ex) {

        log.error("Application not found.");

        return "applicationNotFound";

    }

    @ExceptionHandler(InvalidTokenException.class)
    public String invalidToken(InvalidTokenException ex) {

        log.error("Invalid token.");

        return "invalidToken";

    }

    @ExceptionHandler(Exception.class)
    public String exception(Exception e) {

        log.error("Exception ", e);

        return "somethingWentWrong";

    }

}
