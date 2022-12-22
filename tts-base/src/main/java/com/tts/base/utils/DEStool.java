package com.tts.base.utils;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class DEStool {

    private final static String sKey = "929cf86bb3a3c3eff509f624f48e97e426d7de8a3847fd862293bf9de52d708a";

    private static Cipher makeCipher() throws Exception {
        return Cipher.getInstance("DES");
    }

    private static SecretKey makeKeyFactory() throws Exception {
        SecretKeyFactory des = SecretKeyFactory.getInstance("DES");
        return des.generateSecret(new DESKeySpec(sKey.getBytes()));
    }

    public static String encrypt(String text) throws Exception {
        Cipher cipher = makeCipher();
        SecretKey secretKey = makeKeyFactory();
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return new String(Base64.encodeBase64(cipher.doFinal(text.getBytes())));
    }

    public static String decrypt(String text) throws Exception {
        Cipher cipher = makeCipher();
        SecretKey secretKey = makeKeyFactory();
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return new String(cipher.doFinal(Base64.decodeBase64(text.getBytes())));
    }

}

