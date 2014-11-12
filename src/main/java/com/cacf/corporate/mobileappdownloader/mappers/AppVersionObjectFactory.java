package com.cacf.corporate.mobileappdownloader.mappers;

import com.cacf.corporate.mobileappdownloader.dto.store.AppVersion;
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
public class AppVersionObjectFactory implements ObjectFactory<AppVersion> {

    @Inject
    private TokenService tokenService;

    @Inject
    private HttpServletRequest request;

    @Override
    public AppVersion create(Object source, MappingContext mappingContext) {

        com.cacf.corporate.mobileappdownloader.entities.store.AppVersion src = (com.cacf.corporate.mobileappdownloader.entities.store.AppVersion) source;

        AppVersion result = new AppVersion();

        if (!StringUtils.isEmpty(src.getName())) {
            result.setName(src.getName());
        }

        if (!StringUtils.isEmpty(src.getNumber())) {
            result.setNumber(src.getNumber());
        }

        if (!StringUtils.isEmpty(src.getDescription())) {
            result.setDescription(src.getDescription());
        }


        if (src.getFilesURILocations() != null && src.getFilesURILocations().getIcons() != null) {
            if (src.getFilesURILocations().getIcons().getSmall() != null) {
                result.setIconDownloadUrl("SMALL");
            } else {
                if (src.getFilesURILocations().getIcons().getLarge() != null) {
                    result.setIconDownloadUrl("LARGE");
                }
            }
        }

        return result;

    }

}
