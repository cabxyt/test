package com.fl21.cloud.api.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;


public class MD5Utils {

    public static byte[] getMD5(String content) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("MD5");
        byte[] bytes = digest.digest(content.getBytes("utf8"));
        return bytes;
    }

    private static String byte2hex(byte[] bytes) {
        StringBuilder hex = new StringBuilder();
        for(int i=0; i< bytes.length; i++) {
            byte b = bytes[i];
            int inte = (b < 0)? Math.abs(b)|0x80 : Math.abs(b);
            String temp = Integer.toHexString(inte);
            if(temp.length() == 1) {
                hex.append("0");
            }
            hex.append(temp.toLowerCase());
        }
        return hex.toString();
    }

    public static String getHexMD5(String source)  {
        try {
            byte[] md5 = getMD5(source);
            return byte2hex(md5);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getMd5ByFile(File file) {
        String value = null;
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        try {
            MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(byteBuffer);
            BigInteger bi = new BigInteger(1, md5.digest());
            value = bi.toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return value;
    }

}
