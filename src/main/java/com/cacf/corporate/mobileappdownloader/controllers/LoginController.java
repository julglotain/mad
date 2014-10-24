package com.cacf.corporate.mobileappdownloader.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by cacf on 09/10/14.
 */
@Controller
public class LoginController {

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView index(final HttpServletRequest request) {

        ModelAndView loginView = new ModelAndView("login");

        if(request.getParameterMap().containsKey("error")){
            loginView.addObject("invalidCredentials",true);
        }

        return loginView;
    }

}
