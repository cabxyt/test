package com.fl21.cloud.api.custom;


import com.fl21.cloud.api.util.HttpClientUtils;
import org.apache.http.entity.StringEntity;

import java.util.*;


public class GetHeReportData extends HttpClientUtils {

    String postUrl = "http://dev.feellike21.com/custom/health/report/data";
    String reportIdList = "1,2,3";
    String contentType = "application/x-www-form-urlencoded; charset=UTF-8";
    String partnerUserId = "USER001";
    String token = "";

    public void testGetHeReportData(){

        Map<String,String> postParams = new HashMap<String,String>(){{
            put("report_id_list", reportIdList);
        }};

        Map<String, String> headMap = new HashMap<String, String>(){{
            put("token",token);
            put("Content-Type", contentType);
        }};

        post(postUrl, headMap,
            () -> new StringEntity(getPostStr(postParams), "utf-8"),
            responseEntity -> {
                String entityString = getEntityString(responseEntity);
                System.out.println("result = " + entityString);
            }
        );
    }


}
