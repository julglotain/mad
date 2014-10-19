package com.cacf.corporate.mobileappdownloader.services;

import com.cacf.corporate.mobileappdownloader.dto.ManifestContextConfig;
import com.cacf.corporate.mobileappdownloader.dto.ManifestContextConfigBuilder;
import com.cacf.corporate.mobileappdownloader.param.ConfigConstants;
import oracle.jrockit.jfr.tools.ConCatRepository;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.IContext;
import org.thymeleaf.spring4.SpringTemplateEngine;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
public class PlistManifestGenerator implements ManifestGenerator {

    @Inject
    private SpringTemplateEngine templateEngine;

    @Inject
    private TokenService tokenService;

    @Inject
    private Environment env;

    private static final String IOS_APP_DOWNLOAD_MANIFEST_TEMPLATE_NAME = "ios-app-download-manifest";

    @Override
    public String generate() {

        Map<String, Object> variables = new HashMap<>();

        ManifestContextConfig contextConfig =
                new ManifestContextConfigBuilder()
                        .withAppBundleIdentifier(env.getProperty(ConfigConstants.APP_BUNDLE_INDENTIFIER))
                        .withAppTitle(env.getProperty(ConfigConstants.APP_TITLE))
                        .withProxyDlAppUrl(env.getProperty(ConfigConstants.PROXY_DL_APP_URL))
                        .withProxyDlSmallIconUrl(env.getProperty(ConfigConstants.PROXY_DL_SMALL_ICON_URL))
                        .withProxyDlLargeIconUrl(env.getProperty(ConfigConstants.PROXY_DL_LARGE_ICON_URL))
                        .withToken(tokenService.getCurrentToken().getValue())
                        .createManifestContextConfig();

        variables.put("context", contextConfig);

        return createHtmlContentFromTemplate(Locale.ENGLISH, variables);

    }

    private String createHtmlContentFromTemplate(Locale locale, Map<String, Object> variables) {

        IContext context = new Context(locale, variables);

        return templateEngine.process(IOS_APP_DOWNLOAD_MANIFEST_TEMPLATE_NAME, context);
    }
}
