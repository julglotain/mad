package com.cacf.corporate.mobileappdownloader.utils;

import org.apache.commons.lang3.SystemUtils;

/**
 * Created by jug on 17/11/2014.
 */
public class EnvironmentUtils {

    public static String resolveEnvironmentFilepathScheme() {

        if(SystemUtils.IS_OS_MAC_OSX || SystemUtils.IS_OS_LINUX || SystemUtils.IS_OS_UNIX){
            return "file:";
        }

        if(SystemUtils.IS_OS_WINDOWS){
            return "file:///";
        }

        return "";

    }

}
