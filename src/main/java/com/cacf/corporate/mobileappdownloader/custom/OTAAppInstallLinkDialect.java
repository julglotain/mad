package com.cacf.corporate.mobileappdownloader.custom;

import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.doctype.DocTypeIdentifier;
import org.thymeleaf.doctype.resolution.ClassLoaderDocTypeResolutionEntry;
import org.thymeleaf.doctype.resolution.FileDocTypeResolutionEntry;
import org.thymeleaf.doctype.resolution.IDocTypeResolutionEntry;
import org.thymeleaf.doctype.translation.IDocTypeTranslation;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.spring4.dialect.SpringStandardDialect;

import java.net.URL;
import java.util.*;

/**
 * Created by cacf on 10/10/14.
 */
public class OTAAppInstallLinkDialect extends AbstractDialect {

    public static final DocTypeIdentifier APPLE_PROPERTY_LIST_PUBLICID =
            DocTypeIdentifier.forValue("-//Apple//DTD PLIST 1.0//EN");

    public static final DocTypeIdentifier APPLE_PROPERTY_LIST_SYSTEMID =
            DocTypeIdentifier.forValue("http://www.apple.com/DTDs/PropertyList-1.0.dtd");

    public static final IDocTypeResolutionEntry APPLE_PROPERTY_LIST_DOC_TYPE_RESOLUTION_ENTRY =
            new ClassLoaderDocTypeResolutionEntry(
                    APPLE_PROPERTY_LIST_PUBLICID, // PUBLICID
                    APPLE_PROPERTY_LIST_SYSTEMID, // SYSTEMID
                    "dtd/PropertyList-1.0.dtd"); // CLASS-LOADER-RESOLVABLE RESOURCE

    @Override
    public String getPrefix() {
        return "ota";
    }

    @Override
    public Set<IDocTypeResolutionEntry> getDocTypeResolutionEntries() {

        final Set<IDocTypeResolutionEntry> newDocTypeResolutionEntries = new LinkedHashSet<>();
        newDocTypeResolutionEntries.add(APPLE_PROPERTY_LIST_DOC_TYPE_RESOLUTION_ENTRY);
        return newDocTypeResolutionEntries;

    }

    @Override
    public Set<IProcessor> getProcessors() {
        Set<IProcessor> processors = new HashSet<>();
        processors.add(new IOSDownloadAppLinkProcessor());
        return processors;
    }

    public static void main(String[] args) {



    }


}


