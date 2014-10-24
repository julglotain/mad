package com.cacf.corporate.mobileappdownloader.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by jug on 22/10/2014.
 */
@Controller
public class AccessDeniedController {

    @RequestMapping(value="/accessDenied")
    public String accessDenied(HttpServletResponse response){
        response.setStatus(HttpStatus.FORBIDDEN.value());
        return "accessDenied";
    }

}
