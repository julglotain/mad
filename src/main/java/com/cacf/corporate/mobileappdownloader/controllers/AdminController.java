package com.cacf.corporate.mobileappdownloader.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by jug on 22/10/2014.
 */
@Controller
public class AdminController {

   @RequestMapping(value = "/admin",method = RequestMethod.GET)
   public String view(){
       return "admin";
   }

}
