package com.cacf.corporate.mobileappdownloader;

import com.cacf.corporate.mobileappdownloader.config.AppConfiguration;
import com.cacf.corporate.mobileappdownloader.config.WebMvcConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.*;

/**
 * Created by cacf on 09/10/14.
 */
public class ApplicationInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    private static Logger log = LoggerFactory.getLogger(ApplicationInitializer.class);

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


}
