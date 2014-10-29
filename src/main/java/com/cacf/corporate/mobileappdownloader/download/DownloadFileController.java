package com.cacf.corporate.mobileappdownloader.download;

import com.cacf.corporate.mobileappdownloader.bundles.AppConfigurationTriplet;
import com.cacf.corporate.mobileappdownloader.bundles.BundlesStoreConfigurationManager;
import com.cacf.corporate.mobileappdownloader.bundles.domain.ApplicationConfiguration;
import com.cacf.corporate.mobileappdownloader.services.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResourceAccessException;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URLDecoder;

/**
 * Created by cacf on 13/10/14.
 */
@Controller
public class DownloadFileController {


    private static Logger log = LoggerFactory.getLogger(DownloadFileController.class);

    @Inject
    private TokenService tokenService;

    @Inject
    private BundlesStoreConfigurationManager bundlesStoreConfigurationManager;


    @Inject
    private Environment env;

    public static final String DOWNLOAD_APP_FILE_ROUTE_PATH =
            "/dowload/{token}/bundle/{bundle}/app/{app}/version/{version}/file/{type}";

    @RequestMapping(value = DOWNLOAD_APP_FILE_ROUTE_PATH, method = RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    public ResponseEntity<? extends Resource> download(
            @PathVariable("token") String token,
            @PathVariable("bundle") String bundle,
            @PathVariable("app") String app,
            @PathVariable("version") String version,
            @PathVariable("type") String type) throws InvalidTokenException, IOException, InvalidDownloadFileTypeException, ApplicationConfigurationNotFoundException {

        System.out.println(URLDecoder.decode(app, "utf-8"));

        boolean isTokenValid = tokenService.isValid(token);

        if (!isTokenValid) {

            log.debug("Your download token is invalid.");

            throw new InvalidTokenException("Download token {" + token + "} is invalid.");

        }

        // recherche de la configuration de l'app pour laquelle on souhaite produire un manifest
        AppConfigurationTriplet appConfig = bundlesStoreConfigurationManager.findBy(bundle, URLDecoder.decode(app,"utf-8"), version);

        if (appConfig == null) {

            throw new ApplicationConfigurationNotFoundException("The application was not found, no way to produce a manifest.");

        }

        log.debug("About to download file: {}", type);

        String filePath = resolveFileURI(type, appConfig.getThird());

        Resource resource = new PathMatchingResourcePatternResolver().getResource(filePath);

        if (!resource.exists()) {

            log.error("Fichier non trouvé");

            throw new ResourceAccessException("Fichier non trouvé");

        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(resolveContentType(type));
        headers.setContentDispositionFormData("attachment", resource.getFilename());

        return new ResponseEntity<>(resource, headers, HttpStatus.OK);

    }

    private MediaType resolveContentType(String type) {
        if (type.equals(DownloadFileType.APP.getValue())) {
            return MediaType.APPLICATION_OCTET_STREAM;
        }

        if (type.equals(DownloadFileType.SMALL_ICON.getValue())) {
            return MediaType.IMAGE_PNG;
        }

        if (type.equals(DownloadFileType.LARGE_ICON.getValue())) {
            return MediaType.IMAGE_PNG;
        }

        return null;
    }

    private String resolveFileURI(String fileType, ApplicationConfiguration config) throws InvalidDownloadFileTypeException {

        if (fileType.equals(DownloadFileType.APP.getValue())) {
            return config.getFilesURILocations().getApp();
        }

        if (fileType.equals(DownloadFileType.SMALL_ICON.getValue())) {
            return config.getFilesURILocations().getIcons().getSmall();
        }

        if (fileType.equals(DownloadFileType.LARGE_ICON.getValue())) {
            return config.getFilesURILocations().getIcons().getLarge();
        }

        throw new InvalidDownloadFileTypeException("The type '" + fileType + "' is invalid.");

    }

    @ExceptionHandler(ApplicationConfigurationNotFoundException.class)
    public String applicationNotFoundHandler(ApplicationConfigurationNotFoundException ex) {

        log.error("Application not found: ", ex);

        return "applicationNotFound";

    }


    @ExceptionHandler(InvalidTokenException.class)
    public String invalidToken(InvalidTokenException ex) {

        log.error("InvalidTokenException", ex);

        return "invalidToken";

    }

    @ExceptionHandler(Exception.class)
    public String exception(Exception e) {

        log.debug("Base Exception ", e);

        return "somethingWentWrong";

    }

}
