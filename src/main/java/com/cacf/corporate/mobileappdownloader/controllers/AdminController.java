package com.cacf.corporate.mobileappdownloader.controllers;

import com.cacf.corporate.mobileappdownloader.dto.store.Bundle;
import com.cacf.corporate.mobileappdownloader.entities.store.AppVersion;
import com.cacf.corporate.mobileappdownloader.services.AppVersionAlreadyExistsException;
import com.cacf.corporate.mobileappdownloader.services.AppsStoreService;
import com.cacf.corporate.mobileappdownloader.services.FileWritingFailureException;
import com.cacf.corporate.mobileappdownloader.utils.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import javax.servlet.http.Part;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by jug on 22/10/2014.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    private static final Logger log = LoggerFactory.getLogger(AdminController.class);

    @Inject
    private AppsStoreService appsStoreService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView view() {

        ModelAndView mav = new ModelAndView("admin");

        mav.addObject("availableProfiles", appsStoreService.getAvailableProfiles());

        return mav;

    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> upload(
            @RequestParam("name") String name, @RequestParam("version") String version,
            @RequestParam(value = "desc", required = false) String desc,
            @RequestParam(value = "bundle") String bundle, @RequestParam(value = "profile") String profile,
            @RequestParam("app") Part app,
            @RequestParam(value = "smallIcon", required = false) Part smallIcon,
            @RequestParam(value = "largeIcon", required = false) Part largeIcon) throws AppVersionAlreadyExistsException, FileWritingFailureException {

        Pair<com.cacf.corporate.mobileappdownloader.entities.store.Bundle, AppVersion> newConf = new BindParametersToAppConf(name, version, desc, bundle, profile).invoke();

        appsStoreService.addAppVersion(newConf, app, smallIcon, largeIcon);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("result", "OK");
        responseBody.put("message", "App has been sucessfully added to the store.");
        return new ResponseEntity<>(responseBody, HttpStatus.OK);


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


    @RequestMapping(value = "/bundle", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> searchBundles(@RequestParam("identifier") String identifier) {

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("result", "OK");
        responseBody.put("items", mapToBundlesDTOs(appsStoreService.findBundlesByIdentifierPrefix(identifier)));
        return new ResponseEntity<>(responseBody, HttpStatus.OK);

    }

    private Set<Bundle> mapToBundlesDTOs(Set<com.cacf.corporate.mobileappdownloader.entities.store.Bundle> entities) {

        Set<Bundle> results = new HashSet<>();

        for (com.cacf.corporate.mobileappdownloader.entities.store.Bundle entity : entities) {

            Bundle dto = new Bundle();
            dto.setIdentifier(entity.getIdentifier());
            dto.setProfile(entity.getProfile());

            results.add(dto);

        }

        return results;

    }

    private class BindParametersToAppConf {
        private String name;
        private String version;
        private String desc;
        private String bundle;
        private String profile;

        public BindParametersToAppConf(String name, String version, String desc, String bundle, String profile) {
            this.name = name;
            this.version = version;
            this.desc = desc;
            this.bundle = bundle;
            this.profile = profile;
        }

        public Pair<com.cacf.corporate.mobileappdownloader.entities.store.Bundle, AppVersion> invoke() {
            com.cacf.corporate.mobileappdownloader.entities.store.Bundle newBundleConf = new com.cacf.corporate.mobileappdownloader.entities.store.Bundle();
            newBundleConf.setIdentifier(bundle);
            newBundleConf.setProfile(profile);

            AppVersion appVersionConf = new AppVersion();
            appVersionConf.setNumber(version);
            appVersionConf.setName(name);

            if (!StringUtils.isEmpty(desc)) {
                appVersionConf.setDescription(desc);
            }

            return new Pair<>(newBundleConf, appVersionConf);
        }
    }
}
