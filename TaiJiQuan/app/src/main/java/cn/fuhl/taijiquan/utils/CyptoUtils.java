package cn.fuhl.taijiquan.utils;

import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * TaiJiQuan
 * Description:
 * Created by Fu.H.L on
 * Date:2015-05-22
 * Time:上午10:51
 * Copyright © 2015年 Fu.H.L All rights reserved.
 */
public class CyptoUtils {
    public static final String ALGORITHM_DES = "DES/CBC/PKCS5Padding";

    /**
     * DES算法，加密
     *
     * @param data    待加密字符串
     * @param key     加密私钥，长度不能够小于8位
     * @return        加密后的字节数组，一般结合Base64编码使用
     * @throws        java.security.InvalidAlgorithmParameterException
     * @throws        Exception
     */
    public static String encode(String key,String data) {
        if(data == null)
            return null;
        try{
            DESKeySpec dks = new DESKeySpec(key.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            Key secretKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
            IvParameterSpec iv = new IvParameterSpec("12345678".getBytes());
            AlgorithmParameterSpec paramSpec = iv;
            cipher.init(Cipher.ENCRYPT_MODE, secretKey,paramSpec);
            byte[] bytes = cipher.doFinal(data.getBytes());
            return byte2hex(bytes);
        }catch(Exception e){
            e.printStackTrace();
            return data;
        }
    }

    /**
     * DES算法，解密
     *
     * @param data       待解密字符串
     * @param key        解密私钥，长度不能够小于8位
     * @return           解密后的字节数组
     * @throws Exception 异常
     */
    public static String decode(String key,String data) {
        if(data == null)
            return null;
        try {
            DESKeySpec dks = new DESKeySpec(key.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            Key secretKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
            IvParameterSpec iv = new IvParameterSpec("12345678".getBytes());
            AlgorithmParameterSpec paramSpec = iv;
            cipher.init(Cipher.DECRYPT_MODE, secretKey, paramSpec);
            return new String(cipher.doFinal(hex2byte(data.getBytes())));
        } catch (Exception e){
            e.printStackTrace();
            return data;
        }
    }

    /**
     * 二行制转字符串
     *
     * @param  b  二行制
     * @return
     */
    private static String byte2hex(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; b!=null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1)
                hs.append('0');
            hs.append(stmp);
        }
        return hs.toString().toUpperCase();
    }

    private static byte[] hex2byte(byte[] b) {
        if((b.length%2)!=0)
            throw new IllegalArgumentException();
        byte[] b2 = new byte[b.length/2];
        for (int n = 0; n < b.length; n+=2) {
            String item = new String(b,n,2);
            b2[n/2] = (byte)Integer.parseInt(item,16);
        }
        return b2;
    }
}