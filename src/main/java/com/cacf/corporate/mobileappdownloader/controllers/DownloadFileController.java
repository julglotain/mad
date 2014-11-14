package com.cacf.corporate.mobileappdownloader.controllers;

import com.cacf.corporate.mobileappdownloader.download.DownloadFileType;
import com.cacf.corporate.mobileappdownloader.download.InvalidDownloadFileTypeException;
import com.cacf.corporate.mobileappdownloader.download.InvalidTokenException;
import com.cacf.corporate.mobileappdownloader.entities.store.AppVersion;
import com.cacf.corporate.mobileappdownloader.entities.store.Bundle;
import com.cacf.corporate.mobileappdownloader.services.AppVersionNotFoundException;
import com.cacf.corporate.mobileappdownloader.services.AppsStoreService;
import com.cacf.corporate.mobileappdownloader.services.TokenService;
import com.cacf.corporate.mobileappdownloader.utils.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

/**
 * Created by cacf on 13/10/14.
 */
@Controller
public class DownloadFileController {


    private static Logger log = LoggerFactory.getLogger(DownloadFileController.class);

    @Inject
    private TokenService tokenService;

    @Inject
    private AppsStoreService appsStoreService;

    public static final String DOWNLOAD_APP_FILE_ROUTE_PATH =
            "/store/dl/{token}/bundle/{bundle}/profile/{profile}/version/{version}/file/{type}";

    @RequestMapping(value = DOWNLOAD_APP_FILE_ROUTE_PATH, method = RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    public ResponseEntity<? extends Resource> download(
            @PathVariable("token") String token,
            @PathVariable("bundle") String bundle,
            @PathVariable("profile") String profile,
            @PathVariable("version") String version,
            @PathVariable("type") String type) throws InvalidTokenException, IOException, InvalidDownloadFileTypeException, AppVersionNotFoundException {


        boolean isTokenValid = tokenService.isValid(token);

        if (!isTokenValid) {

            log.debug("Your download token is invalid.");

            throw new InvalidTokenException("Download token {" + token + "} is invalid.");

        }

        // recherche de la configuration de l'app pour laquelle on souhaite produire un manifest
        Pair<AppVersion, Bundle> appVersionConfig = appsStoreService.findAppVersionWithBundle(bundle, profile, version);

        String filePath = resolveFileURI(type, appVersionConfig.getFirst());

        Resource resource = new PathMatchingResourcePatternResolver().getResource(filePath);

        if (!resource.exists()) {

            throw new ResourceAccessException("File not found.");

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


    private String resolveFileURI(String fileType, AppVersion conf) throws InvalidDownloadFileTypeException {

        if (fileType.equals(DownloadFileType.APP.getValue())) {
            return conf.getFilesURILocations().getApp();
        }

        if (fileType.equals(DownloadFileType.SMALL_ICON.getValue())) {
            return conf.getFilesURILocations().getIcons().getSmall();
        }

        if (fileType.equals(DownloadFileType.LARGE_ICON.getValue())) {
            return conf.getFilesURILocations().getIcons().getLarge();
        }

        throw new InvalidDownloadFileTypeException("The type '" + fileType + "' is invalid.");

    }


    @ExceptionHandler(AppVersionNotFoundException.class)
    public String applicationNotFoundHandler(AppVersionNotFoundException ex) {

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
