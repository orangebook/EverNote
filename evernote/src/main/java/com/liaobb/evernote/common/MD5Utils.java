package com.liaobb.evernote.common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 一个用于MD5加密码的工具类
 * Created by S.King on 2015/10/14.
 */
public class MD5Utils {

    public static String getMD5(String val) throws NoSuchAlgorithmException {
        MessageDigest md5 =
                MessageDigest.getInstance("MD5");
          md5.update(val.getBytes());
        byte[] m = md5.digest();//加密
        return getString(m);
    }

    private static String getString(byte[] b)
    {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            sb.append(b[i]);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        try {
            System.out.println(getMD5("TEST"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
