package com.fl21.cloud.api.http;

import com.alibaba.fastjson.JSON;
import com.fl21.cloud.api.busine.GenerateToken;
import com.fl21.cloud.api.busine.GetReportData;
import com.fl21.cloud.api.busine.NetCheck;
import com.fl21.cloud.api.busine.UploadHeReport;
import com.fl21.cloud.api.custom.GetSummaryList;
import com.fl21.cloud.api.sdk.ApiCommand;
import com.fl21.cloud.api.sdk.ApiResult;
import com.fl21.cloud.api.util.Logger;
import fi.iki.elonen.NanoHTTPD;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : Power
 * @date : 2018/3/2
 */
public class HttpServer extends NanoHTTPD {

    private static final Logger LOG = Logger.getLogger(HttpServer.class);

    public HttpServer(int port) {
        super(port);
        LOG.info("API HttpServer start. SEE http://localhost:"+port+"/");
        copyFile();
    }

    private void copyFile() {
        InputStream is = HttpServer.class.getResourceAsStream("/STC.pdf");
        String userDir = System.getProperties().getProperty("user.dir");
        demoFile = new File(userDir + "/STC.pdf");
        FileOutputStream os = null;
        try {
            byte[] buffer = new byte[1024];
            int readBytes;
            os = new FileOutputStream(demoFile);
            while ((readBytes = is.read(buffer)) != -1) {
                os.write(buffer, 0, readBytes);
            }
        } catch(IOException ioe){
            LOG.error("copyFile error: %s",ioe.getMessage());
        }finally {
            try {
                os.close();
                is.close();
            } catch (IOException e) {
            }
        }
        LOG.info("copy demo pdf file to %s", demoFile.getAbsolutePath());
    }

    private String reportIdCache = null;
    private String tokenCache;
    private String defaultServerAddress = "http://192.168.1.20:9001/";
    private File demoFile;

    private static Map<String, String> secretMap = new HashMap<String,String>(){{
        put("FESCO", "1q2w3e4r");
    }};

    @Override
    public void start() throws IOException {
        super.start();
    }

    @Override
    public Response serve(IHTTPSession session) {

        String uri = session.getUri();
        String mimeType = "application/json";
        String respMsg;
        try {
            switch (uri) {
                case "/":{
                    respMsg = getRoutes();
                    mimeType = "text/html";
                    break;
                }
                //生成token
                case "/go_generate_token":
                    respMsg = makeGenerateTokenPage();
                    mimeType = "text/html";
                    break;
                case "/generate_token":
                    respMsg = generateToken(session);
                    break;

                //网络检测
                case "/go_net_check":
                    respMsg = makeNetCheckPage();
                    mimeType = "text/html";
                    break;
                case "/net_check":
                    respMsg = netCheck(session);
                    mimeType = "text/html";
                    break;

                //上传体检报告
                case "/go_upload_report":
                    respMsg = makeUploadPage();
                    mimeType = "text/html";
                    break;
                case "/upload_report":
                    respMsg = uploadReport(session);
                    break;

                //获取报告结果
                case "/go_get_report":
                    mimeType = "text/html";
                    respMsg = makeGetReport();
                    break;
                case "/get_report":
                    respMsg = getReport(session);
                    break;

                //获取体检报告列表数据
                case "/go_summary_list":
                    mimeType = "text/html";
                    respMsg = makeSummaryListPage();
                    break;
                case "/summary_list":
                    respMsg = summaryList(session);
                    break;
                default:
                    respMsg = "{\"result\":false, \"reason\":\"["+uri+"] is not support request.\"}";
            }
        } catch (Exception e) {
            e.printStackTrace();
            respMsg = "server internal error :" + e.getMessage();
        }

        Response resp = newFixedLengthResponse(
                Response.Status.OK,
                mimeType,
                respMsg);

        return resp;
    }



    private String getRoutes() {
        Element html = new Element("html")
            .appendChild(new Element("body")
                .appendChild(new Element("h2").text("API LIST"))
                .appendChild(new Element("h5").text("(v1.1)"))
                .appendChild(new Element("hr"))
                .appendChild(new Element("h4").text("=BUSINE="))
                .appendChild(new Element("ul")
                    .appendChild(new Element("li")
                        .appendChild(new Element("a")
                            .attr("href", "/go_generate_token")
                            .text("Generate Custom Token")
                        )
                    )
                    .appendChild(new Element("li")
                        .appendChild(new Element("a")
                            .attr("href","/go_net_check")
                            .text("Network check")
                        )
                    )
                    .appendChild(new Element("li")
                        .appendChild(new Element("a")
                            .attr("href","/go_upload_report")
                            .text("Upload health report pdf file.")
                        )
                    )
                    .appendChild(new Element("li")
                        .appendChild(new Element("a")
                            .attr("href","/go_get_report")
                            .text("Get report data.")
                        )
                    )
                )
                .appendChild(new Element("hr"))
                .appendChild(new Element("h4").text("=CUSTOM="))
                    .appendChild(new Element("ul")
                        .appendChild(new Element("li")
                            .appendChild(new Element("a")
                                .attr("href", "/go_summary_list")
                                .text("Summary list")
                            )
                        )

                    )
            );
        return html.outerHtml();
    }


    private String makeUploadPage() {
        Element html = new Element("html").attr("class","devise-layout-html")
            .appendChild(new Element("body")
                .appendChild(new Element("head").appendChild(new Element("meta").attr("http-equiv","Content-Type").attr("content","text/html; charset=utf-8")))
                .appendChild(new Element("h2").text("upload pdf file form"))
                .appendChild(new Element("form").attr("action","/upload_report").attr("method","GET")
                    .appendChild(new Element("table")
                        .appendChild(new Element("tr")
                            .appendChild(new Element("td")
                                .appendChild(new TextNode("api address:"))
                            )
                            .appendChild(new Element("td")
                                .appendChild(new Element("input").attr("type","text").attr("name","apiAddress").attr("size","90").val(defaultServerAddress))
                            )
                        )
                        .appendChild(new Element("tr")
                            .appendChild(new Element("td")
                                .appendChild(new TextNode("api version:"))
                            )
                            .appendChild(new Element("td")
                                .appendChild(new Element("input").attr("type","text").attr("name","apiVersion").attr("size","90").val("v1"))
                            )
                        )
                        .appendChild(new Element("tr")
                            .appendChild(new Element("td")
                                .appendChild(new TextNode("appId:"))
                            )
                            .appendChild(new Element("td")
                                .appendChild(new Element("input").attr("type","text").attr("name","appId").attr("size","90").val("FESCO"))
                            )
                        )
                        .appendChild(new Element("tr")
                            .appendChild(new Element("td")
                                .appendChild(new TextNode("appSecret:"))
                            )
                            .appendChild(new Element("td")
                                .appendChild(new Element("input").attr("type","text").attr("name","appSecret").attr("size","90").val("1q2w3e4r"))
                            )
                        )
                        .appendChild(new Element("tr")
                            .appendChild(new Element("td")
                                .appendChild(new TextNode("inst:"))
                            )
                            .appendChild(new Element("td")
                                .appendChild(new Element("input").attr("type","text").attr("name","inst").attr("size","90").val("47"))
                            )
                        )
                        .appendChild(new Element("tr")
                            .appendChild(new Element("td")
                                .appendChild(new TextNode("userId:"))
                            )
                            .appendChild(new Element("td")
                                .appendChild(new Element("input").attr("type","text").attr("name","userId").attr("size","90").val("100001"))
                            )
                        )
                        .appendChild(new Element("tr")
                            .appendChild(new Element("td")
                                .appendChild(new TextNode("file path:"))
                            )
                            .appendChild(new Element("td")
                                .appendChild(new Element("input").attr("type","text").attr("name","path").attr("size","90").val(demoFile.getAbsolutePath()))
                            )
                        )
                        .appendChild(new Element("tr")
                            .appendChild(new Element("td")
                                .appendChild(new TextNode("time:"))
                            )
                            .appendChild(new Element("td")
                                .appendChild(new Element("input").attr("type","text").attr("name","time").attr("size","90").val("20180126"))
                            )
                        )
                        .appendChild(new Element("tr")
                            .appendChild(new Element("td").attr("colspan","2")
                                .appendChild(new Element("input").attr("type","submit").val("submit"))
                            )
                        )

                    )
                )
            );
        return html.outerHtml();
    }

    private String uploadReport(IHTTPSession session) {
        Map<String, String> parms = session.getParms();
        String apiHost    = parms.get("apiAddress");
        String apiVersion = parms.get("apiVersion");
        String appId      = parms.get("appId");
        String appSecret  = parms.get("appSecret");
        String userId     = parms.get("userId");
        String inst       = parms.get("inst");
        String time       = parms.get("time");
        String path       = parms.get("path");
        String host       = apiHost + apiVersion;
        LOG.info("access address head = " + apiHost + apiVersion);
        File file = new File(path);
        ApiCommand uploadCmd = new UploadHeReport(appId,appSecret,host,userId,inst,time,file);
        ApiResult uploadRst = uploadCmd.execute();
        String reportId = uploadRst.safeGetData("reportId");
        if (reportId != null) {
            reportIdCache = reportId;
        }
        LOG.info("upload report = %s, id = %s", uploadRst.getMsg(), reportId);
        return JSON.toJSONString(uploadRst);
    }


    private String makeNetCheckPage() {
        Element html = new Element("html").attr("class","devise-layout-html")
            .appendChild(new Element("body")
                .appendChild(new Element("h2").text("Network check form"))
                .appendChild(new Element("form").attr("action","/net_check").attr("method","GET")
                    .append("api address:")
                    .appendChild(new Element("input").attr("type","text").attr("name","apiAddress").val(defaultServerAddress))
                    .appendChild(new Element("br"))

                    .append("api version:")
                    .appendChild(new Element("input").attr("type","text").attr("name","apiVersion").val("v1"))
                    .appendChild(new Element("br"))

                    .append("institution id:")
                    .appendChild(new Element("input").attr("type","text").attr("name","appId").val("FESCO"))
                    .appendChild(new Element("br"))

                    .append("time:")
                    .appendChild(new Element("input").attr("type","text").attr("name","appSecret").val("20180126"))
                    .appendChild(new Element("br"))

                    .append("file path:")
                    .appendChild(new Element("input").attr("type","text").attr("name","path").val("/tmp/abc.pdf"))
                    .appendChild(new Element("br"))

                    .appendChild(new Element("input").attr("type","submit").val("submit"))
                )
            );
        return html.outerHtml();
    }

    private String netCheck(IHTTPSession session) {
        Map<String, String> parms = session.getParms();
        String apiHost    = parms.get("apiAddress");
        String apiVersion = parms.get("apiVersion");
        String appId      = parms.get("appId");
        String appSecret  = parms.get("appSecret");
        LOG.info("access address head = " + apiHost + apiVersion);
        ApiCommand netCheckCmd = new NetCheck(apiHost+apiVersion, appId, appSecret);
        ApiResult tokenRst = netCheckCmd.execute();
        LOG.info("net check = %s", tokenRst.getMsg());
        return String.valueOf(tokenRst.getMsg());
    }

    private String makeGenerateTokenPage() {
        Element html = new Element("html").attr("class","devise-layout-html")
            .appendChild(new Element("body")
                .appendChild(new Element("h2").text("generate token form"))
                .appendChild(new Element("form").attr("action","/generate_token").attr("method","GET")
                    .append("api address:")
                    .appendChild(new Element("input").attr("type","text").attr("name","apiAddress").val(defaultServerAddress))
                    .appendChild(new Element("br"))

                    .append("api version:")
                    .appendChild(new Element("input").attr("type","text").attr("name","apiVersion").val("v1"))
                    .appendChild(new Element("br"))

                    .append("userId:")
                    .appendChild(new Element("input").attr("type","text").attr("name","userId").val("100001"))
                    .appendChild(new Element("br"))

                    .append("avh:")
                    .appendChild(new Element("input").attr("type","text").attr("name","avh").val("0"))
                    .appendChild(new Element("br"))

                    .append("appId:")
                    .appendChild(new Element("input").attr("type","text").attr("name","appId").val("FESCO"))
                    .appendChild(new Element("br"))

                    .append("appSecret:")
                    .appendChild(new Element("input").attr("type","text").attr("name","appSecret").val("1q2w3e4r"))
                    .appendChild(new Element("br"))

                    .appendChild(new Element("input").attr("type","submit").val("submit"))
                )
            );
        return html.outerHtml();
    }


    private String generateToken(IHTTPSession session) {
        Map<String, String> parms = session.getParms();
        String apiHost    = parms.get("apiAddress");
        String apiVersion = parms.get("apiVersion");
        String userId     = parms.get("userId");
        String avh        = parms.get("avh");
        String appId      = parms.get("appId");
        String appSecret  = parms.get("appSecret");
        LOG.info("access address head = " + apiHost + apiVersion);
        ApiCommand generateToken = new GenerateToken(apiHost+apiVersion, avh, userId, appId, appSecret);
        ApiResult tokenRst = generateToken.execute();
        String token = tokenRst.safeGetData("token");
        if (token != null) {
            tokenCache = token;
        }
        LOG.info("generate token = %s token = %s", tokenRst.getMsg(), token);
        return JSON.toJSONString(tokenRst);
    }

    private String makeGetReport() {
        Element html = new Element("html").attr("class","devise-layout-html")
            .appendChild(new Element("body")
                .appendChild(new Element("h2").text("Get report data form"))
                .appendChild(new Element("form").attr("action","/get_report").attr("method","GET")
                    .append("api address:")
                    .appendChild(new Element("input").attr("type","text").attr("name","apiAddress").val(defaultServerAddress))
                    .appendChild(new Element("br"))

                    .append("api version:")
                    .appendChild(new Element("input").attr("type","text").attr("name","apiVersion").val("v1"))
                    .appendChild(new Element("br"))

                    .append("reportIdList:")
                    .appendChild(new Element("input").attr("type","text").attr("name","reportIdList").val(reportIdCache))
                    .appendChild(new Element("br"))

                    .append("appId:")
                    .appendChild(new Element("input").attr("type","text").attr("name","appId").val("FESCO"))
                    .appendChild(new Element("br"))

                    .append("appSecret:")
                    .appendChild(new Element("input").attr("type","text").attr("name","appSecret").val("1q2w3e4r"))
                    .appendChild(new Element("br"))

                    .appendChild(new Element("input").attr("type","submit").val("submit"))
                )
            );
        return html.outerHtml();
    }


    private String getReport(IHTTPSession session) {
        Map<String, String> parms = session.getParms();
        String apiHost      = parms.get("apiAddress");
        String apiVersion   = parms.get("apiVersion");
        String reportIdList = parms.get("reportIdList");
        String appId        = parms.get("appId");
        String appSecret    = parms.get("appSecret");
        LOG.info("access address head = " + apiHost + apiVersion);
        String host         = apiHost + apiVersion;
        ApiCommand getReportData = new GetReportData(appId, appSecret, host, reportIdList);
        ApiResult getRst = getReportData.execute();
        LOG.info("generate token = %s token = %s", getRst.getMsg(), getRst.safeGetData("token"));
        return JSON.toJSONString(getRst);
    }


    private String makeSummaryListPage() {
        Element html = new Element("html").attr("class","devise-layout-html")
            .appendChild(new Element("body")
                .appendChild(new Element("h2").text("Get report data form"))
                .appendChild(new Element("form").attr("action","/summary_list").attr("method","GET")
                    .append("api address:")
                    .appendChild(new Element("input").attr("type","text").attr("name","apiAddress").val(defaultServerAddress))
                    .appendChild(new Element("br"))

                    .append("api version:")
                    .appendChild(new Element("input").attr("type","text").attr("name","apiVersion").val("v1"))
                    .appendChild(new Element("br"))

                    .append("token:")
                    .appendChild(new Element("input").attr("type","text").attr("name","token").val(tokenCache))
                    .appendChild(new Element("br"))

                    .appendChild(new Element("input").attr("type","submit").val("submit"))

                )
            );
        return html.outerHtml();
    }

    private String summaryList(IHTTPSession session) {
        Map<String, String> parms = session.getParms();
        String apiHost      = parms.get("apiAddress");
        String apiVersion   = parms.get("apiVersion");
        String token        = parms.get("token");
        String host         = apiHost + apiVersion;

        LOG.info("access address head = " + apiHost + apiVersion);
        ApiCommand getSummaryList = new GetSummaryList(host, token);
        ApiResult getRst = getSummaryList.execute();
        LOG.info("generate token = %s", getRst.getMsg());
        return JSON.toJSONString(getRst);
    }


    public static void main(String[] args) throws IOException {
        HttpServer server = new HttpServer(10999);
        server.start(-1, false);
    }



}
