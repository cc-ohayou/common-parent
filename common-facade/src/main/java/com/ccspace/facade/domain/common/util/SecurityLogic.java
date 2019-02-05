package com.ccspace.facade.domain.common.util;


import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * @AUTHOR CF
 * @DATE Created on 2017/9/8 16:21.
 */

public class SecurityLogic {
    static Logger logger = LoggerFactory.getLogger(SecurityLogic.class);
    public static final String TOKEN_AES_KEY = "N13iJFLBjrlBA5aw";

    // 加密
    private static String encryptAES(String sSrc, String sKey) throws Exception {
        byte[] raw = sKey.getBytes("utf-8");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");//"算法/模式/补码方式"
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));

        return new Base64().encodeToString(encrypted);//此处使用BASE64做转码功能，同时能起到2次加密的作用。
    }

    // 解密
    public static String decryptAES(String sSrc, String sKey) throws Exception {
        try {
            byte[] raw = sKey.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] encrypted1 = new Base64().decode(sSrc);//先用base64解密
            try {
                byte[] original = cipher.doFinal(encrypted1);
                String originalString = new String(original, "utf-8");
                return originalString;
            } catch (Exception e) {
                logger.info(e.toString());
                return null;
            }
        } catch (Exception ex) {
            logger.info(ex.toString());
            return null;
        }
    }


    /**
     * 加密 token
     *
     * @param userId
     * @param token
     * @return
     */
    public static String encryptToken(Long userId, String token) {
        String toEncrypt = userId + "|" + token;
        try {
            return encryptAES(toEncrypt, TOKEN_AES_KEY);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 加密密码
     *
     * @param password
     * @return
     */
    public static String encryptPassword(String password) {
        return SecurityUtil.SHA1(SecurityUtil.MD5(password));
    }

    /**
     * @param
     * @return
     * @description md5加密
     * @author CF create on 2017/7/12 9:37
     */
    public static String encryptMD5(String password) {
        return SecurityUtil.MD5(password);
    }

    public static String sha1Encrypt(String password) {
        return SecurityUtil.SHA1(password);
    }

    public static void main(String[] args) throws Exception {
//		System.out.println(encryptPassword("123456"));
        System.out.println(SecurityUtil.MD5("123456"));//e10adc3949ba59abbe56e057f20f883e
        System.out.println(SecurityUtil.SHA1("e10adc3949ba59abbe56e057f20f883e"));//e10adc3949ba59abbe56e057f20f883e
//		System.out.println(SecurityUtil.SHA1("123456"));
    }
}

