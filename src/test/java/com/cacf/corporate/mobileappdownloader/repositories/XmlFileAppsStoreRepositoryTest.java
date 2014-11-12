package com.cacf.corporate.mobileappdownloader.repositories;

import com.cacf.corporate.mobileappdownloader.config.MarshallingConfiguration;
import com.cacf.corporate.mobileappdownloader.config.PropertiesConfiguration;
import com.cacf.corporate.mobileappdownloader.config.RepositoriesConfiguration;
import com.cacf.corporate.mobileappdownloader.config.ResourcesConfiguration;
import com.cacf.corporate.mobileappdownloader.entities.store.AppsStore;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;

/**
 * Created by jug on 06/11/2014.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RepositoriesConfiguration.class, MarshallingConfiguration.class, ResourcesConfiguration.class, PropertiesConfiguration.class})
public class XmlFileAppsStoreRepositoryTest {


    @Inject
    private XmlFileAppsStoreRepository repo;

    @Test
    public void should_load_apps_store() {

        AppsStore store  = repo.getConfig();

        Assert.assertEquals("Devrait etre egal à 2",2,store.getBundles().size());

    }


}
