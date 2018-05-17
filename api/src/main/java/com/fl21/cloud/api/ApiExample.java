package com.fl21.cloud.api;

import com.fl21.cloud.api.busine.GenerateToken;
import com.fl21.cloud.api.busine.GetReportData;
import com.fl21.cloud.api.busine.NetCheck;
import com.fl21.cloud.api.busine.UploadHeReport;
import com.fl21.cloud.api.custom.GetSummaryList;
import com.fl21.cloud.api.sdk.ApiCommand;
import com.fl21.cloud.api.sdk.ApiResult;
import com.fl21.cloud.api.util.Logger;
import com.fl21.cloud.api.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Created by shenli on 2018/1/30.
 */
public class ApiExample {

    private static final Logger log = Logger.getLogger(ApiExample.class);

    public static void main(String[] args) throws IOException, InterruptedException {


        String appId = StringUtils.getArgs(args, 0, "FESCO");
        String appSecret = StringUtils.getArgs(args, 1, "1q2w3e4r");
        String apiAddress = StringUtils.getArgs(args, 2, "http://dev.feellike21.com/");
        String apiVersion = "v1";
        String userId = StringUtils.getArgs(args, 3, "100002");
        String host = apiAddress + apiVersion;


        //net check
        ApiCommand netCheck = new NetCheck(host, appId, appSecret);
        ApiResult netState = netCheck.execute();
        log.info("net check = "+netState.getMsg());


        //generate token
        String avh = "10";
        ApiCommand generateToken = new GenerateToken(host, avh, "c2fcb87dcd63decb1e576cec8097847e", appId, appSecret);
        ApiResult tokenRst = generateToken.execute();
        String token = tokenRst.safeGetData("token");
        log.info("generate token = %s token = %s", tokenRst.getMsg(), token);


        //upload report
        String inst = "47";
        String time = "20180126";
        URL resource = ApiExample.class.getResource("/STC.pdf");
        String path = resource.getFile();
        log.info("file.path = %s", path);
        File file = new File(path);
        ApiCommand upload = new UploadHeReport(appId,appSecret,host,userId,inst,time,file);
        ApiResult uploadRst = upload.execute();
        String reportId = uploadRst.safeGetData("reportId");
        log.info("upload report = %s, id = %s", uploadRst.getMsg(), reportId);

        log.info("please wait 10 s ...");
        Thread.sleep(10000L);

        //get report data
        //String reportIdList = reportId;32565242540065792
        String reportIdList = "32565242540065792,32594464219333632";
        ApiCommand getRptData = new GetReportData(appId,appSecret,host,reportIdList);
        ApiResult reportData = getRptData.execute();
        log.info("get report = %s, content = %s",reportData.getMsg(),reportData.getData().toString());


        //custom get health report data (H5 use it)
        ApiCommand customGetAggrData = new GetSummaryList(host,token);
        ApiResult data = customGetAggrData.execute();
        log.info("custom get summary list = %s, content= %s", data.getMsg(), String.valueOf(data.getData()));



    }
}
