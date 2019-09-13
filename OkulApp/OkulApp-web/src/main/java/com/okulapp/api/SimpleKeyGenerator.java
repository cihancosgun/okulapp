/*
 
 
 
 */
package com.okulapp.api;

import java.security.Key;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Doruk Fişek
 */
public class SimpleKeyGenerator implements KeyGenerator {

    // ======================================
    // =          Business methods          =
    // ======================================
    @Override
    public Key generateKey() {
        String keyString = "okulappdummykey";
        Key key = new SecretKeySpec(keyString.getBytes(), 0, keyString.getBytes().length, "DES");
        return key;
    }
}
