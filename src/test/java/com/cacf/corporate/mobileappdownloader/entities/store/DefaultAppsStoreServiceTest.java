package com.cacf.corporate.mobileappdownloader.entities.store;

import com.cacf.corporate.mobileappdownloader.repositories.AppsStoreRepository;
import com.cacf.corporate.mobileappdownloader.security.User;
import com.cacf.corporate.mobileappdownloader.services.DefaultAppsStoreService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

/**
 * Created by jug on 10/11/2014.
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultAppsStoreServiceTest {

    @InjectMocks
    private DefaultAppsStoreService service;

    @Mock
    private AppsStoreRepository repo;

    @Before
    public void setup() {

        when(repo.getConfig()).thenReturn(createAppsStoreFixture());

    }

    @Test
    public void should_filter_bundles_accessible_for_specific_user() {


        AppsStore result = service.getUserRightsFilteredAppsStore(createSimpleUser());

        assertThat(result.getBundles().size(), is(3));


    }

    private AppsStore createAppsStoreFixture() {

        AppsStore fixture = new AppsStore();

        Set<Bundle> appsStoreBundles = new HashSet<>();

        appsStoreBundles.add(new Bundle("com.cacf.creditplus.dev", "DEV"));
        appsStoreBundles.add(new Bundle("com.cacf.creditplus.rct", "RCT"));
        appsStoreBundles.add(new Bundle("com.cacf.creditplus", "PROD"));

        appsStoreBundles.add(new Bundle("com.cacf.agos", "PROD"));
        appsStoreBundles.add(new Bundle("com.cacf.agos.rct", "RCT"));

        appsStoreBundles.add(new Bundle("com.truc.bidule.dev", "DEV"));

        fixture.setBundles(appsStoreBundles);

        return fixture;
    }

    private User createSimpleUser() {

        User u = new User();

        Set<String> userBaseBundleNames = new HashSet<>();

        userBaseBundleNames.add("com.cacf.creditplus");
        userBaseBundleNames.add("com.cacf.agos");

        u.setBundles(userBaseBundleNames);

        u.getProfiles().add("DEV");
        u.getProfiles().add("RCT");

        return u;

    }

}
