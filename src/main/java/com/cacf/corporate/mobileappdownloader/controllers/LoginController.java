package com.cacf.corporate.mobileappdownloader.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by cacf on 09/10/14.
 */
@Controller
public class LoginController {

    @RequestMapping(value="/login",method = RequestMethod.GET)
    public String index(){
        return "login";
    }

}
