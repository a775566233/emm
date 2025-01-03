package com.emm.util.string;

import com.emm.config.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class Digest {
    private static AppConfig appConfig;
    @Autowired
    public void setAppConfig(AppConfig appConfig) {
        Digest.appConfig = appConfig;
    }

    public static String encrypt(String content, String algorithm, String charsets) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance(algorithm);
        byte[] hash = md.digest(content.getBytes(charsets));
        StringBuffer sb = new StringBuffer();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    public static String encrypt(String content, String algorithm, String charsets, int frequency) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        for (int i = 0; i < frequency; i++) {
            content = encrypt(content, algorithm, charsets);
        }
        return content;
    }

    public static String encrypt(String content, String algorithm, String charsets, int frequency, String prefixSalt, String suffixSalt) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        content = addSalt(content, prefixSalt, suffixSalt);
        return encrypt(content, algorithm, charsets, frequency);
    }

    public static String addSalt(String content, String prefixSalt, String suffixSalt) {
        return prefixSalt + content + suffixSalt;
    }

    public static String encryptUserPassword(String userPassword) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        return encrypt(
                userPassword,
                appConfig.getUserPasswordEncryption(),
                appConfig.getEncodingType(),
                appConfig.getUserPasswordEncryptionFrequency(),
                appConfig.getUserPasswordPrefixSalt(),
                appConfig.getUserPasswordSuffixSalt()
                );
    }

    public static String encryptUserPhone(String userPhone) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        return encrypt(
                userPhone,
                appConfig.getUserPhoneEncryption(),
                appConfig.getEncodingType(),
                appConfig.getUserPhoneFrequency(),
                appConfig.getUserPhonePrefixSalt(),
                appConfig.getUserPhoneSuffixSalt()
        );
    }
}
