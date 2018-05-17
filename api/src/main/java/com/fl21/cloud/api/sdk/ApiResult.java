package com.fl21.cloud.api.sdk;


import com.alibaba.fastjson.JSONObject;

public class ApiResult {

    private String code;
    private String msg;
    private JSONObject data;

    public ApiResult() {
    }

    public ApiResult(String code, String msg, JSONObject data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

    public String safeGetData(String key) {
        if (data == null) {
            return null;
        }
        return data.getString(key);
    }
}
