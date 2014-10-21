package com.cacf.corporate.mobileappdownloader.controllers;

import com.cacf.corporate.mobileappdownloader.download.OTAManifestProducerController;
import com.cacf.corporate.mobileappdownloader.services.TokenService;
import com.cacf.corporate.mobileappdownloader.utils.URLBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by cacf on 10/10/14.
 */
@Controller
public class GetDownloadLinkController {

    private static Logger log = LoggerFactory.getLogger(GetDownloadLinkController.class);

    @Inject
    private TokenService tokenService;

    @RequestMapping(value = {"/"}, method = RequestMethod.GET)
    public String root() {

        return "redirect:/download";

    }

    @RequestMapping(value = {"/download"}, method = RequestMethod.GET)
    public ModelAndView get(HttpServletRequest request) {

        String downloadManifestUrl = buildManifestDownloadUrl(tokenService.getCurrentToken().getValue(), request
        );

        ModelAndView mav = new ModelAndView();
        mav.setViewName("downloadApp");
        mav.addObject("downloadManifestUrl", downloadManifestUrl);

        return mav;

    }

    private String buildManifestDownloadUrl(String token, HttpServletRequest request) {

        URLBuilder builder = new URLBuilder();
        builder
                .setScheme(request.getScheme())
                .setServerName(request.getServerName())
                .setServerPort(request.getServerPort())
                .setContextPath(request.getContextPath())
                .setPath(OTAManifestProducerController.DOWNLOAD_MANIFEST_ROUTE_PATH.replace("{token}", token));

        return builder.build();
    }


}
