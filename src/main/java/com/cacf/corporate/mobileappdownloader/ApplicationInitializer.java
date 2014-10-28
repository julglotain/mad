package com.cacf.corporate.mobileappdownloader;

import com.cacf.corporate.mobileappdownloader.config.AppConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletRegistration;

/**
 * Created by jug on 27/10/2014.
 */
public class ApplicationInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    private static Logger log = LoggerFactory.getLogger(ApplicationInitializer.class);

    private int maxUploadSizeInMb = 100 * 1024 * 1024; // 100 MB

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[]{AppConfiguration.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[0];
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    @Override
    protected void customizeRegistration(ServletRegistration.Dynamic registration) {

        MultipartConfigElement multipartConfigElement =
                new MultipartConfigElement("/tmp",
                        maxUploadSizeInMb, maxUploadSizeInMb * 2, maxUploadSizeInMb / 2);

        registration.setMultipartConfig(multipartConfigElement);

    }
}
