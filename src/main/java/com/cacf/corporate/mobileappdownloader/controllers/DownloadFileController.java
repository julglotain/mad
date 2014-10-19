package com.cacf.corporate.mobileappdownloader.controllers;

import com.cacf.corporate.mobileappdownloader.param.ConfigConstants;
import com.cacf.corporate.mobileappdownloader.services.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
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
    private Environment env;

    @RequestMapping(value = "/download/{token}/file/{type}", method = RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    public ResponseEntity<? extends Resource> download(@PathVariable("token") String token, @PathVariable("type") String type) throws InvalidTokenException, IOException, InvalidDownloadFileTypeException {

        boolean isTokenValid = tokenService.isValid(token);

        if (!isTokenValid) {

            log.debug("Your download token is invalid.");

            throw new InvalidTokenException("Download token {" + token + "} is invalid.");

        }

        log.debug("About to download file: {}", type);

        String filePath = resolveFileURI(type);

        Resource resource = new PathMatchingResourcePatternResolver().getResource(filePath);

        if(!resource.exists()){

            log.error("Fichier non trouvé");

            throw new ResourceAccessException("Fichier non trouvé");

        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(resolveContentType(type));
        headers.setContentDispositionFormData("attachment",resource.getFilename());

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

    private String resolveFileURI(String type) throws InvalidDownloadFileTypeException {

        if (type.equals(DownloadFileType.APP.getValue())) {
            return env.getProperty(ConfigConstants.FILE_APP_URI);
        }

        if (type.equals(DownloadFileType.SMALL_ICON.getValue())) {
            return env.getProperty(ConfigConstants.FILE_SMALL_ICON_URI);
        }

        if (type.equals(DownloadFileType.LARGE_ICON.getValue())) {
            return env.getProperty(ConfigConstants.FILE_LARGE_ICON_URI);
        }

        throw new InvalidDownloadFileTypeException("The type '" + type + "' is invalid.");

    }

    @ExceptionHandler(InvalidTokenException.class)
    public String invalidToken(InvalidTokenException ex) {

        log.error("InvalidTokenException",ex);

        return "invalidToken";

    }

    @ExceptionHandler(Exception.class)
    public String exception(Exception e) {

        log.debug("Base Exception ", e);

        return "somethingWentWrong";

    }

}
