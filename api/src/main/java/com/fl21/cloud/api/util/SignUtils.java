package com.fl21.cloud.api.util;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by shenli on 2018/1/22.
 */
public class SignUtils {

    private static final Logger log = Logger.getLogger(SignUtils.class);

    public static String genSign(TreeMap<String, String> params, String appId, String appSecret) {
        params.put("appid", appId);
        StringBuilder preStr = new StringBuilder();
        preStr.append(appSecret);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            String val = entry.getValue();
            if (StringUtils.isNotBlank(val)) {
                preStr.append(key).append(val);
            }
        }
        log.debug("TestSignUtil.preStr=%s",preStr.toString());
        String mySign = MD5Utils.getHexMD5(preStr.toString());
        return mySign;
    }

}
