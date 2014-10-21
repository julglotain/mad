package com.cacf.corporate.mobileappdownloader.marshalling;

import com.cacf.corporate.mobileappdownloader.marshalling.XmlMarshaller;
import com.thoughtworks.xstream.XStream;

import java.io.File;

/**
 * Created by jug on 20/10/2014.
 */
public class XStreamXmlMarshaller implements XmlMarshaller {

    private XStream delegate = new XStream();

    @Override
    public void registerAlias(String name, Class clazz){
        delegate.alias(name,clazz);
    }

    @Override
    public String toXML(Object source) {
        return delegate.toXML(source);
    }

    @Override
    public Object fromXML(String source) {
        return delegate.fromXML(source);
    }

    @Override
    public Object fromXML(File source) {
        return delegate.fromXML(source);
    }

    @Override
    public void useAttributeFor(Class clazz, String attr) {
        delegate.useAttributeFor(clazz,attr);
    }

    @Override
    public void addImplicitCollection(Class clazz, String field) {
        delegate.addImplicitCollection(clazz,field);
    }
}
