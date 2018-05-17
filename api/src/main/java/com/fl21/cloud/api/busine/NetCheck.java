package com.fl21.cloud.api.busine;


import com.alibaba.fastjson.JSONObject;
import com.fl21.cloud.api.sdk.ApiCommand;
import com.fl21.cloud.api.sdk.ApiResult;
import com.fl21.cloud.api.util.HttpClientUtils;
import com.fl21.cloud.api.util.Logger;
import com.fl21.cloud.api.util.SignUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by shenli on 2018/1/23.
 */
public class NetCheck extends HttpClientUtils implements ApiCommand {

    private String getUrl;
    private String appId;
    private String appSecret;
    private String contentType = "application/x-www-form-urlencoded; charset=UTF-8";

    private static Logger log = Logger.getLogger(NetCheck.class);

    public NetCheck(String host, String appId,String appSecret) {
        this.appId = appId;
        this.appSecret = appSecret;
        this.getUrl = host + "/busine/common/netcheck";
    }

    @Override
    public ApiResult execute() {
        return netCheck();
    }


    public ApiResult netCheck(){

        ApiResult result = new ApiResult();

        TreeMap<String,String> getParams = new TreeMap<>();

        String sign = SignUtils.genSign(getParams, appId, appSecret);

        Map<String, String> headMap = new HashMap<String, String>(){{
            put("appid", appId);
            put("sign",sign);
            put("Content-Type", contentType);
        }};

        super.get(getUrl,headMap,getParams,responseEntity -> {
            String entityString = getEntityString(responseEntity);
            log.debug("response str = %s", entityString);
            JSONObject jo = JSONObject.parseObject(entityString);
            result.setCode(jo.getString("code"));
            result.setMsg(jo.getString("msg"));
        });

        return result;

    }



}
