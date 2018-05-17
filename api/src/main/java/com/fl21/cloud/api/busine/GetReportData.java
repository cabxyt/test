package com.fl21.cloud.api.busine;

import com.fl21.cloud.api.sdk.ApiCommand;
import com.fl21.cloud.api.sdk.ApiResult;
import com.fl21.cloud.api.util.HttpClientUtils;
import com.fl21.cloud.api.util.JSONUtils;
import com.fl21.cloud.api.util.Logger;
import com.fl21.cloud.api.util.SignUtils;
import org.apache.http.entity.StringEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by shenli on 2018/1/23.
 */
public class GetReportData extends HttpClientUtils implements ApiCommand{

    String postUrl;
    String appId ;
    String secret;
    String contentType = "application/x-www-form-urlencoded; charset=UTF-8";
    String reportIdList;

    private static final Logger log = Logger.getLogger(GetReportData.class);

    public GetReportData(String appId, String secret, String host, String reportIdList) {
        this.appId = appId;
        this.postUrl = host+"/busine/health/report/aggr/list";
        this.secret = secret;
        this.reportIdList = reportIdList;
    }

    @Override
    public ApiResult execute() {
        return getReportData();
    }

    public ApiResult getReportData(){

        ApiResult result = new ApiResult();

        TreeMap<String,String> postParams = new TreeMap<String,String>(){{
            put("report_id_list", reportIdList);
        }};

        String sign = SignUtils.genSign(postParams, appId, secret);

        Map<String, String> headMap = new HashMap<String, String>(3){{
            put("appid", appId);
            put("sign",sign);
            put("Content-Type", contentType);
        }};


        post(postUrl, headMap,
            () -> new StringEntity(getPostStr(postParams), "utf-8"),
            responseEntity -> {
                String entityString = getEntityString(responseEntity);
                log.debug("result = %s", entityString);
                JSONUtils.parseResult(entityString, result);
            }
        );

        return result;
    }
}
