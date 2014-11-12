package com.cacf.corporate.mobileappdownloader.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Part;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by jug on 22/10/2014.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    private static final Logger log = LoggerFactory.getLogger(AdminController.class);

    // @Inject
    // private BundlesStoreConfigurationManager bundlesStoreConfigurationManager;

    @Value("${mad.apps-bundle-store.uploaded.dir.path}")
    private String uploadAppsStoreBaseDir;

    @RequestMapping(method = RequestMethod.GET)
    public String view() {
        return "admin";
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> upload(
            @RequestParam("title") String title, @RequestParam("version") String version,
            @RequestParam(value = "desc", required = false) String desc,
            @RequestParam(value = "bundle") String bundle, @RequestParam(value = "profileId", required = false) String profileId,
            @RequestParam("app") Part app,
            @RequestParam(value = "smallIcon", required = false) Part smallIcon,
            @RequestParam(value = "largeIcon", required = false) Part largeIcon) {


        // bundlesStoreConfigurationManager.addApplication(title, version, desc, bundle, profileId, app, smallIcon, largeIcon);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("result", "OK");
        responseBody.put("message", "Application added.");
        return new ResponseEntity<>(responseBody, HttpStatus.OK);


    }

    @RequestMapping(value = "/bundle", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> searchBundles(@RequestParam("identifier") String idenfifier) throws InterruptedException {

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("result", "OK");
        responseBody.put("items", new HashSet<>());//bundlesStoreConfigurationManager.findBundlesByName(idenfifier));
        return new ResponseEntity<>(responseBody, HttpStatus.OK);

    }

}
