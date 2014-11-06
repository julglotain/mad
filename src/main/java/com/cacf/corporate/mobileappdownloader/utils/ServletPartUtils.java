package com.cacf.corporate.mobileappdownloader.utils;

import javax.servlet.http.Part;

/**
 * Created by jug on 04/11/2014.
 */
public class ServletPartUtils {

    public static String getFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        System.out.println("content-disposition header= "+contentDisp);
        String[] tokens = contentDisp.split(";");
        for (String token : tokens) {
            if (token.trim().startsWith("filename")) {
                return token.substring(token.indexOf("=") + 2, token.length()-1);
            }
        }
        return "";
    }

}
