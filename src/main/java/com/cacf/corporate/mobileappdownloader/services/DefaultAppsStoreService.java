package com.cacf.corporate.mobileappdownloader.services;

import com.cacf.corporate.mobileappdownloader.entities.store.AppVersion;
import com.cacf.corporate.mobileappdownloader.entities.store.AppsStore;
import com.cacf.corporate.mobileappdownloader.entities.store.Bundle;
import com.cacf.corporate.mobileappdownloader.repositories.AppsStoreRepository;
import com.cacf.corporate.mobileappdownloader.security.User;
import com.cacf.corporate.mobileappdownloader.utils.Pair;
import com.cacf.corporate.mobileappdownloader.utils.ServletPartUtils;
import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import ma.glasnost.orika.impl.util.StringUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.inject.Inject;
import javax.servlet.http.Part;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;

/**
 * Created by jug on 06/11/2014.
 */
@Service
public class DefaultAppsStoreService implements AppsStoreService {

    private static final Logger log = LoggerFactory.getLogger(DefaultAppsStoreService.class);

    private static final String ALL = "ALL";

    @Value("${mad.apps-bundle-store.uploaded.dir.path}")
    private String uploadAppsStoreBaseDir;

    // private String ON_SERVER_HOST = "{ON_SERVER_HOST}";

    @Inject
    private AppsStoreRepository repository;

    @Override
    public AppsStore load() {
        return repository.getConfig();
    }

    @Override
    public AppsStore getUserRightsFilteredAppsStore(User usr) {

        AppsStore filteredAppsStore = new AppsStore();

        Set<Bundle> filteredBundles = filterByUserRightsAccess(usr);

        filteredAppsStore.setBundles(filteredBundles);

        return filteredAppsStore;
    }

    @Override
    public AppVersion addAppVersion(Pair<Bundle, AppVersion> appConf, Part app, Part smallIcon, Part largeIcon) throws AppVersionAlreadyExistsException, FileWritingFailureException {


        // check if a version already exist for the bundle passed as arg
        if (repository.checkIfVersionAlreadyExists(appConf)) {
            throw new AppVersionAlreadyExistsException();
        }

        // get files directory destination
        final String filesDirectoryDestination = getFilesDirectoryDestination(appConf);

        String appFilepath = null;
        String smallIconFilepath = null;
        String largeIconFilepath = null;

        // write app version files
        try {

            appFilepath = writeFile(filesDirectoryDestination, app);
            smallIconFilepath = writeFile(filesDirectoryDestination, smallIcon);
            largeIconFilepath = writeFile(filesDirectoryDestination, largeIcon);


        } catch (IOException e) {

            log.error("Error occured while attempting to write one of the app file.", e);

            throw new FileWritingFailureException();

        }

        // load store
        AppsStore storeConfig = load();

        // and look for a potential existing bundle
        Bundle existingBundle = lookForBundle(storeConfig.getBundles(), appConf.getFirst().getIdentifier(), appConf.getFirst().getProfile());

        // we build a new AppVersion entity
        AppVersion newAppVersion = createNewAppVersion(appConf.getSecond(), appFilepath, smallIconFilepath, largeIconFilepath);

        if (existingBundle != null) {

            existingBundle.getVersions().add(newAppVersion);

        } else {

            Bundle newBundle = new Bundle(appConf.getFirst().getIdentifier(), appConf.getFirst().getProfile());

            newBundle.getVersions().add(newAppVersion);

            storeConfig.getBundles().add(newBundle);

        }

        // finally we persist the new apps store configuration
        repository.persistConfig(storeConfig);

        return newAppVersion;
    }

    private AppVersion createNewAppVersion(AppVersion appVersionBase, String appFilepath, String smallIconFilepath, String largeIconFilepath) {

        AppVersion.FilesURILocations uriLocations = new AppVersion.FilesURILocations();

        uriLocations.setApp(appFilepath);

        if(!StringUtils.isEmpty(smallIconFilepath) || !StringUtils.isEmpty(largeIconFilepath)){

            AppVersion.FilesURILocations.Icons iconsLocation = new AppVersion.FilesURILocations.Icons();

            if(!StringUtils.isEmpty(smallIconFilepath)){
                iconsLocation.setSmall(smallIconFilepath);
            }

            if(!StringUtils.isEmpty(largeIconFilepath)){
                iconsLocation.setLarge(largeIconFilepath);
            }

            uriLocations.setIcons(iconsLocation);

        }

        return appVersionBase;
    }

    private String writeFile(String filesDirectoryDestination, Part fileToWrite) throws IOException {

        if (fileToWrite != null) {

            File holder = new File(filesDirectoryDestination + File.separator + ServletPartUtils.getFileName(fileToWrite));

            OutputStream out = new FileOutputStream(holder);

            IOUtils.copyLarge(fileToWrite.getInputStream(), out);

            return holder.getAbsolutePath();

        }


        return null;
    }

    private String getFilesDirectoryDestination(Pair<Bundle, AppVersion> appConf) {

        StringBuffer path = new StringBuffer(uploadAppsStoreBaseDir)
                .append(File.separator + appConf.getFirst().getIdentifier().replace(".", File.separator))
                .append(File.separator + appConf.getFirst().getProfile())
                .append(File.separator + appConf.getSecond().getNumber());

        File dirDest = new File(path.toString());

        if (dirDest.exists()) {
            try {
                FileUtils.cleanDirectory(dirDest);
            } catch (IOException e) {
            }
        }

        dirDest.mkdirs();

        return dirDest.getAbsolutePath();
    }

    private Bundle lookForBundle(Set<Bundle> allBundles, String identifier, String profile) {
        for (Bundle bundle : allBundles) {
            if (bundle.getIdentifier().equals(bundle) && bundle.getProfile().equalsIgnoreCase(profile)) {
                return bundle;
            }
        }
        return null;
    }


    @Override
    public AppVersion findAppVersion(String bundle, String profile, String versionNumber) throws AppVersionNotFoundException {

        Set<Bundle> allBundles = load().getBundles();

        for (Bundle bund : allBundles) {
            if (bund.getIdentifier().equals(bundle) && bund.getProfile().equalsIgnoreCase(profile)) {
                for (AppVersion appVersion : bund.getVersions()) {
                    if (appVersion.equals(versionNumber)) {
                        return appVersion;
                    }
                }
            }
        }

        throw new AppVersionNotFoundException(bundle, profile, versionNumber);

    }

    @Override
    public Pair<AppVersion, Bundle> findAppVersionWithBundle(String bundle, String profile, String versionNumber) throws AppVersionNotFoundException {

        Set<Bundle> allBundles = load().getBundles();

        for (Bundle bund : allBundles) {

            if (bund.getIdentifier().equals(bundle) && bund.getProfile().equalsIgnoreCase(profile)) {

                for (AppVersion appVersion : bund.getVersions()) {

                    if (appVersion.getNumber().equals(versionNumber)) {
                        return new Pair<>(appVersion, bund);
                    }

                }

            }

        }

        throw new AppVersionNotFoundException(bundle, profile, versionNumber);

    }

    @Override
    public Set<Bundle> findBundlesByIdentifierPrefix(String idPrefix) {

        Set<Bundle> allBundles = load().getBundles();

        return filterBundlesByIdentifierPrefix(allBundles, idPrefix);

    }

    private Set<Bundle> filterBundlesByIdentifierPrefix(Set<Bundle> bundles, final String identifierPrefix) {

        return Sets.filter(bundles, new Predicate<Bundle>() {

            @Override
            public boolean apply(Bundle input) {

                return input.getIdentifier().startsWith(identifierPrefix);

            }

        });
    }

    private Set<Bundle> filterByUserRightsAccess(final User user) {

        Set<Bundle> currentConfig = load().getBundles();

        Set<Bundle> filteredBundles = (!user.getBundles().contains(ALL))
                ? filterBundlesByUserBundleBasenames(user, currentConfig) : currentConfig;

        return (!user.getProfiles().contains(ALL))
                ? filterBundlesByUserProfiles(user, filteredBundles) : filteredBundles;

    }

    private Set<Bundle> filterBundlesByUserProfiles(final User usr, Set<Bundle> bundles) {

        return Sets.filter(bundles, new Predicate<Bundle>() {

            @Override
            public boolean apply(Bundle input) {

                return usr.getProfiles().contains(input.getProfile());

            }

        });
    }

    private Set<Bundle> filterBundlesByUserBundleBasenames(final User usr, Set<Bundle> bundles) {

        return Sets.filter(bundles, new Predicate<Bundle>() {

            @Override
            public boolean apply(Bundle input) {

                for (String bundleBasename : usr.getBundles()) {
                    if (input.belongsToBaseBundle(bundleBasename)) {
                        return true;
                    }
                }

                return false;
            }

        });

    }

    @Override
    public Set<String> getAvailableProfiles() {

        return repository.getConfig().getAvailableProfiles();
    }
}
