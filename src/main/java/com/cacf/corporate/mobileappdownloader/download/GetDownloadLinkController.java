package com.cacf.corporate.mobileappdownloader.download;

import com.cacf.corporate.mobileappdownloader.bundles.BundlesStoreConfigurationManager;
import com.cacf.corporate.mobileappdownloader.bundles.domain.BundlesStoreConfiguration;
import com.cacf.corporate.mobileappdownloader.bundles.dto.Application;
import com.cacf.corporate.mobileappdownloader.bundles.dto.Bundle;
import com.cacf.corporate.mobileappdownloader.bundles.dto.BundlesStore;
import com.cacf.corporate.mobileappdownloader.bundles.dto.Profile;
import com.cacf.corporate.mobileappdownloader.services.TokenService;
import com.cacf.corporate.mobileappdownloader.usermanagement.User;
import com.cacf.corporate.mobileappdownloader.utils.ProtectedResourceURLBuilder;
import ma.glasnost.orika.BoundMapperFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by cacf on 10/10/14.
 */
@Controller
public class GetDownloadLinkController {

    private static Logger log = LoggerFactory.getLogger(GetDownloadLinkController.class);

    @Inject
    private TokenService tokenService;

    @Inject
    @Qualifier("bundlesConfigMapper")
    private BoundMapperFacade<BundlesStoreConfiguration, BundlesStore> mapperFacade;

    @Inject
    private BundlesStoreConfigurationManager bundlesManager;

    @Inject
    private HttpServletRequest request;

    @RequestMapping(value = {"/"}, method = RequestMethod.GET)
    public String redirectToDownload() {
        return "redirect:/download";
    }


    @RequestMapping(value = {"/download"}, method = RequestMethod.GET)
    public ModelAndView view() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("downloadApp");
        return mav;
    }

    @RequestMapping(value = {"/download.json"}, method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<BundlesStore> data() {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        BundlesStoreConfiguration userFilteredBundlesStore = bundlesManager.filterByUserAccessRights(user);

        BundlesStore userBundleStore = mapperFacade.map(userFilteredBundlesStore);

        computeProtectedResourcesURL(userBundleStore);

        return new ResponseEntity<BundlesStore>(userBundleStore, HttpStatus.OK);

    }

    private void computeProtectedResourcesURL(BundlesStore userBundleStore) {

        for (Bundle bundle : userBundleStore.getBundles()) {

            for (Profile profile : bundle.getProfiles()) {

                for (Application app : profile.getApplications()) {

                    buildProtecteResourcesUrl(bundle, profile, app);

                }

            }

        }

    }

    private void buildProtecteResourcesUrl(Bundle bundle, Profile profile, Application app) {

        ProtectedResourceURLBuilder builder = buildBaseResourcesUrl();

        // build manifest dl url
        builder.setPath(OTAManifestProducerController.DOWNLOAD_APP_MANIFEST_ROUTE_PATH);

        Map<String, String> builderPathValues = new HashMap<>();
        builderPathValues.put("bundle", bundle.getIdentifier() + resolveBundleSuffix(profile.getIdentifierSuffix()));

        try {
            builderPathValues.put("app", URLEncoder.encode(app.getTitle(), "utf-8"));
        } catch (UnsupportedEncodingException e) {
            log.error("Error when trying to encode app title with value '{}'.", app.getTitle());
        }

        builderPathValues.put("version", app.getVersion());
        app.setDlManifestUrl(builder.build(builderPathValues));

        if (app.getDlPreviewIconUrl() != null) {

            builder.setPath(DownloadFileController.DOWNLOAD_APP_FILE_ROUTE_PATH);

            if (app.getDlPreviewIconUrl().equals("SMALL")) {
                builderPathValues.put("type", "SMALL_ICON");
            } else {
                builderPathValues.put("type", "LARGE_ICON");
            }

            app.setDlPreviewIconUrl(builder.build(builderPathValues));
        }


    }

    private String resolveBundleSuffix(String bundleIdentifierProfileSuffix) {
        if (bundleIdentifierProfileSuffix != null) {
            return "." + bundleIdentifierProfileSuffix;
        } else {
            return "";
        }
    }

    private ProtectedResourceURLBuilder buildBaseResourcesUrl() {

        ProtectedResourceURLBuilder builder = new ProtectedResourceURLBuilder();
        builder
                .setScheme(request.getScheme())
                .setServerName(request.getServerName())
                .setServerPort(request.getServerPort())
                .setContextPath(request.getContextPath())
                .withToken(tokenService.getCurrentToken().getValue());

        return builder;
    }


}