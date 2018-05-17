package com.fl21.cloud.api.util;

/**
 * Created by shenli on 2018/1/30.
 */
public class StringUtils {


    public static boolean isNotBlank(String s) {
        return s != null && !"".equals(s);
    }


    public static String getArgs(String[] args, int index, String dftStr) {
        if (args == null) {
            return dftStr;
        }
        if (args.length < index + 1) {
            return dftStr;
        }
        return args[index];
    }
}
