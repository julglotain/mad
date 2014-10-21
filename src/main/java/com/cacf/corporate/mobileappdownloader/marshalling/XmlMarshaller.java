package com.cacf.corporate.mobileappdownloader.marshalling;

import java.io.File;

/**
 * Created by jug on 20/10/2014.
 */
public interface XmlMarshaller {

    String toXML(Object source);

    Object fromXML(String source);

    Object fromXML(File source);

    void registerAlias(String name, Class clazz);

    void useAttributeFor(Class clazz, String attr);

    void addImplicitCollection(Class clazz, String field);

}
