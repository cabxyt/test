package com.fl21.cloud.api.busine;

import com.fl21.cloud.api.sdk.ApiCommand;
import com.fl21.cloud.api.sdk.ApiResult;
import com.fl21.cloud.api.util.HttpClientUtils;
import com.fl21.cloud.api.util.JSONUtils;
import com.fl21.cloud.api.util.Logger;
import com.fl21.cloud.api.util.SignUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Created by shenli on 2018/1/22.
 */
public class GenerateToken extends HttpClientUtils implements ApiCommand{

    String postUrl = "";
    String appId = "";
    String userId = "";
    String avh = "";
    String secret;
    String contentType = "application/x-www-form-urlencoded; charset=UTF-8";

    private Logger log = Logger.getLogger(GenerateToken.class);

    public GenerateToken(String host, String avh, String userId, String appId, String secret) {
        this.postUrl = host + "/busine/common/gentoken";
        this.avh = avh;
        this.userId = userId;
        this.appId = appId;
        this.secret = secret;
    }

    @Override
    public ApiResult execute() {
        return generateToken();
    }

    public ApiResult generateToken(){

        ApiResult result = new ApiResult();

        TreeMap<String,String> postParams = new TreeMap<String,String>(){{
            put("user_id", userId);
            put("avh", avh);
        }};

        String sign = SignUtils.genSign(postParams, appId, secret);

        Map<String, String> headMap = new HashMap<String, String>(){{
            put("appid", appId);
            put("sign",sign);
            put("Content-Type", contentType);
        }};


        post(postUrl, headMap,
            () -> {
                List<NameValuePair> nvps = postParams.entrySet().stream()
                        .map(entry -> new BasicNameValuePair(entry.getKey(), entry.getValue()))
                        .collect(Collectors.toList());
                try {
                    return new UrlEncodedFormEntity(nvps);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return null;
            },
            responseEntity -> {
                String entityString = getEntityString(responseEntity);
                log.debug("response str = %s", entityString);
                JSONUtils.parseResult(entityString, result);
            }
        );

        return result;

    }




}
