package com.cacf.corporate.mobileappdownloader.services;

import com.cacf.corporate.mobileappdownloader.bundles.AppConfigurationTriplet;
import com.cacf.corporate.mobileappdownloader.bundles.domain.ApplicationConfiguration;
import com.cacf.corporate.mobileappdownloader.download.DownloadFileController;
import com.cacf.corporate.mobileappdownloader.download.DownloadFileType;
import com.cacf.corporate.mobileappdownloader.dto.ManifestContextConfig;
import com.cacf.corporate.mobileappdownloader.dto.ManifestContextConfigBuilder;
import com.cacf.corporate.mobileappdownloader.utils.ProtectedResourceURLBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.IContext;
import org.thymeleaf.spring4.SpringTemplateEngine;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
public class PlistManifestGenerator implements ManifestGenerator {

    private static Logger log = LoggerFactory.getLogger(PlistManifestGenerator.class);

    private static final String IOS_APP_DOWNLOAD_MANIFEST_TEMPLATE_NAME = "ios-app-download-manifest";

    @Inject
    private SpringTemplateEngine templateEngine;

    @Inject
    private TokenService tokenService;

    @Inject
    private Environment env;

    @Inject
    private HttpServletRequest request;


    @Override
    public String generate() {
        return null;
    }

    @Override
    public String generate(AppConfigurationTriplet appConfig) {

        ManifestContextConfigBuilder contextConfigBuilder = new ManifestContextConfigBuilder()
                .withAppBundleIdentifier(appConfig.getFirst().getIdentifier() + resolveBundleSuffix(appConfig.getSecond().getIdentifierSuffix()))
                .withAppTitle(appConfig.getThird().getTitle());

        ProtectedResourceURLBuilder resourceURLBuilder = buildBaseResourcesUrl();
        // common path to download any file
        resourceURLBuilder.setPath(DownloadFileController.DOWNLOAD_APP_FILE_ROUTE_PATH);

        Map<String, String> builderPathValues = new HashMap<>();
        builderPathValues.put("bundle", appConfig.getFirst().getIdentifier() + resolveBundleSuffix(appConfig.getSecond().getIdentifierSuffix()));

        try {
            builderPathValues.put("app", URLEncoder.encode(appConfig.getThird().getTitle(), "utf-8"));
        } catch (UnsupportedEncodingException e) {
            log.error("Error when trying to encode app title with value '{}'.", appConfig.getThird().getTitle());
        }
        builderPathValues.put("version", appConfig.getThird().getVersion());

        // build manifest dl url
        builderPathValues.put("type", "APP");
        contextConfigBuilder.withProxyDlAppUrl(resourceURLBuilder.build(builderPathValues));

        // build icons dl url if exist
        if (appConfig.getThird().getFilesURILocations().getIcons() != null) {

            ApplicationConfiguration.FilesURILocations.Icons icons = appConfig.getThird().getFilesURILocations().getIcons();

            if (!StringUtils.isEmpty(icons.getSmall())) {
                builderPathValues.put("type", DownloadFileType.SMALL_ICON.getValue());
                contextConfigBuilder.withProxyDlSmallIconUrl(resourceURLBuilder.build(builderPathValues));
            }
            if (!StringUtils.isEmpty(icons.getLarge())) {
                builderPathValues.put("type", DownloadFileType.LARGE_ICON.getValue());
                contextConfigBuilder.withProxyDlLargeIconUrl(resourceURLBuilder.build(builderPathValues));
            }

        }

        ManifestContextConfig manifestContextConfig = contextConfigBuilder.build();

        Map<String, Object> variables = new HashMap<>();
        variables.put("context", manifestContextConfig);

        return createHtmlContentFromTemplate(Locale.ENGLISH, variables);
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

    private void buildProtecteResourcesUrl(AppConfigurationTriplet appConfig) {

    }

    private String createHtmlContentFromTemplate(Locale locale, Map<String, Object> variables) {

        IContext context = new Context(locale, variables);

        return templateEngine.process(IOS_APP_DOWNLOAD_MANIFEST_TEMPLATE_NAME, context);
    }
}
