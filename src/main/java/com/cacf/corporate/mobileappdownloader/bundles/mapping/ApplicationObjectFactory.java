package com.cacf.corporate.mobileappdownloader.bundles.mapping;

import com.cacf.corporate.mobileappdownloader.bundles.domain.ApplicationConfiguration;
import com.cacf.corporate.mobileappdownloader.bundles.dto.Application;
import com.cacf.corporate.mobileappdownloader.services.TokenService;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.ObjectFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by jug on 23/10/2014.
 */
@Component
public class ApplicationObjectFactory implements ObjectFactory<Application> {

    @Inject
    private TokenService tokenService;

    @Inject
    private HttpServletRequest request;

    @Override
    public Application create(Object source, MappingContext mappingContext) {

        ApplicationConfiguration src = (ApplicationConfiguration) source;

        Application result = new Application();

        if (!StringUtils.isEmpty(src.getTitle())) {
            result.setTitle(src.getTitle());
        }
        if (!StringUtils.isEmpty(src.getVersion())) {
            result.setVersion(src.getVersion());
        }
        if (!StringUtils.isEmpty(src.getDesc())) {
            result.setDesc(src.getDesc());
        }

        if (src.getFilesURILocations() != null && src.getFilesURILocations().getIcons() != null) {
            if (src.getFilesURILocations().getIcons().getSmall() != null) {
                result.setDlPreviewIconUrl("SMALL");
            } else {
                if (src.getFilesURILocations().getIcons().getLarge() != null) {
                    result.setDlPreviewIconUrl("LARGE");
                }
            }
        }

        return result;

    }

}
