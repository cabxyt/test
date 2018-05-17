package com.fl21.cloud.api.busine;

import com.fl21.cloud.api.sdk.ApiCommand;
import com.fl21.cloud.api.sdk.ApiResult;
import com.fl21.cloud.api.util.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by shenli on 2018/1/22.
 */
public class UploadHeReport extends HttpClientUtils implements ApiCommand {


    String postURI= "";
    String appId = "FESCO";
    String userId = "USER001";
    String inst = "FESCO";
    String secret;
    String time = "";
    String md5 = "";
    File file;

    private static final Logger log = Logger.getLogger(UploadHeReport.class);


    public UploadHeReport(String appId, String secret, String host, String userId, String inst, String time, File file) {
        this.appId = appId;
        this.postURI = host +"/busine/health/report/upload";
        this.userId = userId;
        this.inst = inst;
        this.time = time;
        this.file = file;
        this.secret = secret;
        if (file == null || !file.exists()) {
            throw new IllegalArgumentException("file is required!");
        }
        this.md5 = MD5Utils.getMd5ByFile(file);
    }

    @Override
    public ApiResult execute() {
        return uploadReportPDF();
    }

    public ApiResult uploadReportPDF(){

        ApiResult result = new ApiResult();

        TreeMap<String,String> postParams = new TreeMap<String,String>(){{
            put("user_id", userId);
            put("inst", inst);
            put("time", time);
            put("md5", md5);
        }};

        String queryStr = getQueryStr(postParams);
        postURI = postURI + queryStr;

        String sign = SignUtils.genSign(postParams, appId, secret);

        Map<String, String> headMap = new HashMap<String,String>(){{
            put("sign", sign);
            put("appid", appId);
        }};

        postFile(postURI,headMap,postParams,file,responseEntity -> {
            String entityString = super.getEntityString(responseEntity);
            log.debug("entityString = %s", entityString);
            JSONUtils.parseResult(entityString,result);
        });
        return result;

    }


}
