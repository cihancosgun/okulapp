/*
 * 
 * 
 * 
 */
package com.okulapp.expopush;

/**
 *
 * @author Cihan Coşgun 2019 TSPB web:https://www.tspb.org.tr
 * mailto:cihan_cosgun@outlook.com
 */
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

public class NoOpHostnameVerifier implements HostnameVerifier {

    @Override
    public boolean verify(String s, SSLSession sslSession) {
        return true;
    }
}
