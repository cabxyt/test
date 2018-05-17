package com.fl21.cloud.api.custom;

import com.fl21.cloud.api.sdk.ApiCommand;
import com.fl21.cloud.api.sdk.ApiResult;
import com.fl21.cloud.api.util.HttpClientUtils;
import com.fl21.cloud.api.util.JSONUtils;
import com.fl21.cloud.api.util.Logger;
import org.apache.http.entity.StringEntity;

import java.util.HashMap;
import java.util.Map;


public class GetSummaryList extends HttpClientUtils implements ApiCommand{

    String postUrl;
    String contentType = "application/json; charset=UTF-8";
    String token = "";

    public GetSummaryList(String host, String token) {
        this.postUrl = host + "/custom/health/report/aggr/summarylist";
        this.token = token;
    }

    private static final Logger log = Logger.getLogger(GetSummaryList.class);

    @Override
    public ApiResult execute() {
        return getHeReportData();
    }

    private ApiResult getHeReportData(){

        ApiResult result = new ApiResult();

        Map<String,String> postParams = new HashMap<String,String>(1){{
        }};

        Map<String, String> headMap = new HashMap<String, String>(2){{
            put("token",token);
            put("Content-Type", contentType);
        }};

        post(postUrl, headMap,
            () -> new StringEntity(getPostStr(postParams), "utf-8"),
            responseEntity -> {
                String entityString = getEntityString(responseEntity);
                log.info("entityString = %s", entityString);
                JSONUtils.parseResult(entityString,result);
            }
        );
        return result;
    }


}
