package com.cacf.corporate.mobileappdownloader.controllers;

import com.cacf.corporate.mobileappdownloader.dto.store.AppVersion;
import com.cacf.corporate.mobileappdownloader.dto.store.Bundle;
import com.cacf.corporate.mobileappdownloader.entities.store.AppsStore;
import com.cacf.corporate.mobileappdownloader.security.User;
import com.cacf.corporate.mobileappdownloader.services.AppsStoreService;
import com.cacf.corporate.mobileappdownloader.services.TokenService;
import com.cacf.corporate.mobileappdownloader.utils.ProtectedResourceURLBuilder;
import ma.glasnost.orika.BoundMapperFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by cacf on 10/10/14.
 * <p/>
 * - Build Manifest dl url
 * - Build Icons dl url
 * - Build app dl url
 * - Find an app version
 */
@Controller
public class AppsStoreController {

    private static Logger log = LoggerFactory.getLogger(AppsStoreController.class);

    @Inject
    private TokenService tokenService;

    @Inject
    @Qualifier("bundlesMapperFacade")
    private BoundMapperFacade<com.cacf.corporate.mobileappdownloader.entities.store.Bundle, Bundle> bundlesMapperFacade;

    @Inject
    private AppsStoreService appsStoreService;

    @Inject
    private HttpServletRequest request;

    @RequestMapping(value = {"/"}, method = RequestMethod.GET)
    public String redirectToDownload() {
        return "redirect:/store";
    }


    @RequestMapping(value = {"/store"}, method = RequestMethod.GET)
    public ModelAndView view() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("store");
        return mav;
    }

    @RequestMapping(value = {"/store/data"}, method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> data() {


        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        AppsStore userfilteredAppsStore = appsStoreService.getUserRightsFilteredAppsStore(user);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("result", "OK");

        responseBody.put("bundles", mapBundles(userfilteredAppsStore.getBundles()));

        return new ResponseEntity<>(responseBody, HttpStatus.OK);


    }

    private Set<Bundle> mapBundles(Set<com.cacf.corporate.mobileappdownloader.entities.store.Bundle> entities) {

        Set<Bundle> results = new HashSet<>();

        for (com.cacf.corporate.mobileappdownloader.entities.store.Bundle entity : entities) {

            Bundle dto = bundlesMapperFacade.map(entity);

            processManifestAndIconDownloadUrl(dto);

            results.add(dto);
        }

        return results;

    }

    private void processManifestAndIconDownloadUrl(Bundle dto) {


        for (AppVersion appVersion : dto.getVersions()) {

               buildProtecteResourcesUrl(dto.getIdentifier(),dto.getProfile(),appVersion);

        }


    }

    private void buildProtecteResourcesUrl(String bundle, String profile, AppVersion app) {

        ProtectedResourceURLBuilder builder = buildBaseResourcesUrl();

        // build manifest dl url
        builder.setPath(OTAManifestProducerController.DOWNLOAD_APP_MANIFEST_ROUTE_PATH);

        Map<String, String> builderPathValues = new HashMap<>();
        builderPathValues.put("bundle", bundle);
        builderPathValues.put("profile",profile);

        builderPathValues.put("version", app.getNumber());
        app.setManifestDownloadUrl(builder.build(builderPathValues));

        if (app.getIconDownloadUrl() != null) {

            builder.setPath(DownloadFileController.DOWNLOAD_APP_FILE_ROUTE_PATH);

            if (app.getIconDownloadUrl().equals("SMALL")) {
                builderPathValues.put("type", "SMALL_ICON");
            } else {
                builderPathValues.put("type", "LARGE_ICON");
            }

            app.setIconDownloadUrl(builder.build(builderPathValues));
        }


    }

    private ProtectedResourceURLBuilder buildBaseResourcesUrl() {

        ProtectedResourceURLBuilder builder = new ProtectedResourceURLBuilder();
        builder
                .setScheme(request.getScheme())
                .setServerName(request.getServerName())
                .setServerPort(request.getServerPort())
                .setContextPath(request.getContextPath())
                .withToken(tokenService.getCurrentToken().getValue());

        return builder;
    }

}
