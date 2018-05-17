package com.fl21.cloud.api.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fl21.cloud.api.sdk.ApiResult;

/**
 * Created by shenli on 2018/1/30.
 */
public class JSONUtils {

    public static void parseResult(String entityString,ApiResult result) {
        JSONObject jo = JSON.parseObject(entityString);
        result.setCode(jo.getString("code"));
        result.setMsg(jo.getString("msg"));
        String dataStr = jo.getString("data");
        if (dataStr == null) {
            result.setData(new JSONObject());
        }
        else {
            parse(dataStr, result);
        }
    }

    private static void parse(String dataStr, ApiResult result) {
        if(dataStr.startsWith("{")) {
            result.setData(JSON.parseObject(dataStr));
        }
        else if(dataStr.startsWith("[")){
            result.setData(new JSONObject(){{
                put("list", JSON.parseArray(dataStr));}});
        }
    }
}
