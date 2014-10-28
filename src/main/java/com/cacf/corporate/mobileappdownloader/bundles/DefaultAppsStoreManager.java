package com.cacf.corporate.mobileappdownloader.bundles;

import org.springframework.stereotype.Component;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by jug on 27/10/2014.
 */
@Component
public class DefaultAppsStoreManager implements AppsStoreManager {

    @Override
    public String createBundleDirectory(String title, String version, String bundle, String profileId) {

        MessageDigest md = null;

        try {
            md = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        StringBuffer sb = new StringBuffer()
                .append(title)
                .append(version)
                .append(bundle)
                .append(profileId);

        return getHashedString(md.digest(sb.toString().getBytes()));



    }

    private String getHashedString(byte[] bytes) {

        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            if ((0xff & bytes[i]) < 0x10) {
                hexString.append("0"
                        + Integer.toHexString((0xFF & bytes[i])));
            } else {
                hexString.append(Integer.toHexString(0xFF & bytes[i]));
            }
        }

        return hexString.toString();

    }

    public static void main(String[] args) {
        System.out.println(File.separator);
    }

}
