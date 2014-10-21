package com.cacf.corporate.mobileappdownloader.controllers;

import com.cacf.corporate.mobileappdownloader.bundles.BundlesStoreConfigurationManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;

/**
 * Created by cacf on 09/10/14.
 */
@Controller
public class LoginController {

    @Inject
    private BundlesStoreConfigurationManager manager;

    @RequestMapping(value="/login",method = RequestMethod.GET)
    public String index(){

        manager.getConfig();

        return "login";
    }

}
