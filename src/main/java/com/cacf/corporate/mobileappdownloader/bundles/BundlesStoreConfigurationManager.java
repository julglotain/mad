package com.cacf.corporate.mobileappdownloader.bundles;

import com.cacf.corporate.mobileappdownloader.bundles.domain.ApplicationConfiguration;
import com.cacf.corporate.mobileappdownloader.bundles.domain.BundleConfiguration;
import com.cacf.corporate.mobileappdownloader.bundles.domain.BundlesStoreConfiguration;
import com.cacf.corporate.mobileappdownloader.bundles.domain.ProfileConfiguration;
import com.cacf.corporate.mobileappdownloader.dto.BundleProfileCouple;
import com.cacf.corporate.mobileappdownloader.services.AppsFilesManager;
import com.cacf.corporate.mobileappdownloader.usermanagement.User;
import com.cacf.corporate.mobileappdownloader.utils.ServletPartUtils;
import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by jug on 20/10/2014.
 * <p/>
 * TODO Gérer l'ordre des PROFILES retournés, donner un poids au profile pour ordonner selon le poids et non alphabetiquement
 */
@Component
public class BundlesStoreConfigurationManager {

    private static final Logger log = LoggerFactory.getLogger(BundlesStoreConfigurationManager.class);

    @Inject
    private ConfigFilterer filterDelegate;


    @Inject
    private BundleConfigurationRepository repository;

    @Inject
    private AppsFilesManager appsFilesManager;

    public BundlesStoreConfiguration getConfig() {
        return this.repository.getConfig();
    }

    public void setConfig(BundlesStoreConfiguration config) {
        this.repository.setConfig(config);
    }

    public void persistConfig(BundlesStoreConfiguration config) {

        synchronized (getConfig()) {

            this.repository.persistConfig(config);

        }

    }

    public BundlesStoreConfiguration filterByUserAccessRights(User userInfo) {
        return filterDelegate.filter(getConfig(), userInfo);
    }


    public AppConfigurationTriplet findBy(String fullBundleIdentifier, String appTitle, String version) {

        BundlesStoreConfiguration config = getConfig();

        // deduction du profile à partir du full bundle identifier
        String bundleIdentifier = getBundleIdentifier(fullBundleIdentifier);
        String bundleIdentifierSuffix = getBundleIdentifierSuffix(fullBundleIdentifier);

        for (BundleConfiguration bundleConf : config.getBundlesList()) {

            if (bundleConf.getIdentifier().equals(bundleIdentifier) || bundleConf.getIdentifier().equals(fullBundleIdentifier)) {

                for (ProfileConfiguration profileConf : bundleConf.getProfilesList()) {

                    if ((profileConf.getIdentifierSuffix() != null && profileConf.getIdentifierSuffix().equals(bundleIdentifierSuffix))
                            || checkIfBundleWithNoIdentifierSuffix(fullBundleIdentifier, bundleConf.getIdentifier(), profileConf.getIdentifierSuffix())) {

                        for (ApplicationConfiguration appConf : profileConf.getAppsConfigList()) {

                            if (appConf.getTitle().equals(appTitle) && appConf.getVersion().equals(version)) {

                                return new AppConfigurationTriplet(bundleConf, profileConf, appConf);

                            }

                        }

                    }

                }

            }

        }

        return null;

    }

    private boolean checkIfBundleWithNoIdentifierSuffix(String fullBundleIdentifier, String bundleIdentifier, String identifierSuffix) {
        return fullBundleIdentifier.equals(bundleIdentifier) && identifierSuffix == null;
    }

    private String getBundleIdentifier(String bundleIdentifier) {
        return bundleIdentifier.substring(0, bundleIdentifier.lastIndexOf("."));
    }

    private String getBundleIdentifierSuffix(String bundleIdentifier) {
        return bundleIdentifier.substring(bundleIdentifier.lastIndexOf(".") + 1, bundleIdentifier.length());
    }

    public synchronized void addApplication(String title, String version, String desc, String bundle, String profileId, Part app, Part smallIcon, Part largeIcon) {


        // get a directory path based on MD5 digest of app title, version, bundle and profileId
        final String uploadDirectoryPath = appsFilesManager.createBundleDirectory(title, version, bundle, profileId);

        System.out.println(uploadDirectoryPath);

        File uploadDirectory = new File(uploadDirectoryPath);

        File appFile = null;
        File smallIconFile = null;
        File largeIconFile = null;

        // check if directory already exists, if true then delete it
        if (uploadDirectory.exists()) {

            System.out.println("existe, suppression");

            try {
                FileUtils.deleteDirectory(uploadDirectory);
            } catch (IOException e) {
            }

        }

        // creation des repertoires
        uploadDirectory.mkdirs();

        boolean isFileWrittingSuccessFull = false;

        try {

            appFile = new File(uploadDirectoryPath + File.separator + ServletPartUtils.getFileName(app));

            FileUtils.copyInputStreamToFile(app.getInputStream(), appFile);

            if (smallIcon != null) {

                smallIconFile = new File(uploadDirectoryPath + File.separator + ServletPartUtils.getFileName(smallIcon));

                FileUtils.copyInputStreamToFile(smallIcon.getInputStream(), smallIconFile);
            }

            if (largeIcon != null) {

                largeIconFile = new File(uploadDirectoryPath + File.separator + ServletPartUtils.getFileName(largeIcon));

                FileUtils.copyInputStreamToFile(largeIcon.getInputStream(), largeIconFile);
            }

            isFileWrittingSuccessFull = true;

        } catch (IOException e) {

            log.error("Error when writing new file.", e);

        }

        // ecriture de la nouvelle entrée dans le fichier de conf si l'ecriture des
        // fichiers dans le FS s'est bien déroulée
        if (isFileWrittingSuccessFull) {


            BundlesStoreConfiguration config = getConfig();

            BundleConfiguration existingBundleConfig = config.findBundleByIdentifier(bundle);

            if (existingBundleConfig != null) {

                ProfileConfiguration existingProfileConfig = existingBundleConfig.findProfileById(profileId);

                if (existingProfileConfig != null) {

                    existingProfileConfig.getAppsConfigList().add(createNewAppConfiguration(title, version, desc, appFile, smallIconFile, largeIconFile));

                } else {

                    ProfileConfiguration newProfile = createNewProfileConfiguration(profileId, title, version, desc, appFile, smallIconFile, largeIconFile);

                    existingBundleConfig.getProfilesList().add(newProfile);

                }

            } else {

                BundleConfiguration newBundleConfig = new BundleConfiguration();
                newBundleConfig.setIdentifier(bundle);

                newBundleConfig.getProfilesList().add(createNewProfileConfiguration(profileId, title, version, desc, appFile, smallIconFile, largeIconFile));

                config.getBundlesList().add(newBundleConfig);

            }

            // finally persist config
            persistConfig(config);

        }

    }

    private ProfileConfiguration createNewProfileConfiguration(String profileId, String title, String version, String desc, File appFile, File smallIconFile, File largeIconFile) {
        ProfileConfiguration newProfile = new ProfileConfiguration();
        newProfile.setId(profileId);
        newProfile.setIdentifierSuffix(profileId.toLowerCase());
        newProfile.getAppsConfigList().add(createNewAppConfiguration(title, version, desc, appFile, smallIconFile, largeIconFile));
        return newProfile;
    }

    private ApplicationConfiguration createNewAppConfiguration(String title, String version, String desc, File appFile, File smallIconFile, File largeIconFile) {

        ApplicationConfiguration newAppConfig = new ApplicationConfiguration();

        newAppConfig.setTitle(title);
        newAppConfig.setVersion(version);
        newAppConfig.setDesc(desc);

        ApplicationConfiguration.FilesURILocations filesURILocations = new ApplicationConfiguration.FilesURILocations();
        filesURILocations.setApp("file:" + appFile.getAbsolutePath());

        if (smallIconFile != null || largeIconFile != null) {

            ApplicationConfiguration.FilesURILocations.Icons icons = new ApplicationConfiguration.FilesURILocations.Icons();

            if (smallIconFile != null) {
                icons.setSmall("file:" + smallIconFile.getAbsolutePath());
            }

            if (largeIconFile != null) {
                icons.setLarge("file:" + largeIconFile.getAbsolutePath());
            }

            filesURILocations.setIcons(icons);

        }

        newAppConfig.setFilesURILocations(filesURILocations);

        return newAppConfig;
    }

    public Set<BundleProfileCouple> findBundlesByName(final String identifier) {

        BundlesStoreConfiguration config = getConfig();

        Set<BundleProfileCouple> results = new TreeSet<>();

        Set<BundleConfiguration> potentialBundles = Sets.filter(config.getBundlesList(), new Predicate<BundleConfiguration>() {
            @Override
            public boolean apply(BundleConfiguration input) {
                return input.getIdentifier().contains(identifier);
            }
        });

        for (BundleConfiguration bundle : potentialBundles) {

            if (!bundle.getProfilesList().isEmpty()) {

                for (ProfileConfiguration profile : bundle.getProfilesList()) {

                    results.add(new BundleProfileCouple(bundle.getIdentifier(), profile.getId()));

                }

            } else {

                results.add(new BundleProfileCouple(bundle.getIdentifier(), null));

            }


        }

        return results;

    }


}
