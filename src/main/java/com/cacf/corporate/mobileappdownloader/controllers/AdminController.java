package com.cacf.corporate.mobileappdownloader.controllers;

import com.cacf.corporate.mobileappdownloader.bundles.AppsStoreManager;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;

/**
 * Created by jug on 22/10/2014.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    private static final Logger log = LoggerFactory.getLogger(AdminController.class);

    @Inject
    private AppsStoreManager appsStoreManager;

    @Value("${mad.apps-bundle-store.uploaded.dir.path}")
    private String uploadAppsStoreBaseDir;

    @RequestMapping(method = RequestMethod.GET)
    public String view() {
        return "admin";
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ModelAndView upload(
            @RequestParam("title") String title, @RequestParam("version") String version,
            @RequestParam(value = "desc", required = false) String desc,
            @RequestParam(value = "bundle") String bundle, @RequestParam("profileId") String profileId,
            @RequestParam("app") Part app,
            @RequestParam(value = "small_icon", required = false) Part smallIcon,
            @RequestParam(value = "large_icon", required = false) Part largeIcon) {

        ModelAndView mav = new ModelAndView("admin");

        // get a directory path based on MD5 digest of app title, version, bundle and profileId
        final String uploadDirectoryPath = uploadAppsStoreBaseDir + File.separator + appsStoreManager.createBundleDirectory(title, version, bundle, profileId);

        System.out.println(uploadDirectoryPath);

        File uploadDirectory = new File(uploadDirectoryPath);


        // check if directory already exists, if true then delete it
        if (uploadDirectory.exists()) {

            System.out.println("existe, suppression");

            try {
                FileUtils.deleteDirectory(uploadDirectory);
            } catch (IOException e) {}

        }

        try {
            FileUtils.copyInputStreamToFile(app.getInputStream(),new File(uploadDirectoryPath + File.separator + app.getSubmittedFileName()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // write change to apps_store config file



        return mav;

    }


}
