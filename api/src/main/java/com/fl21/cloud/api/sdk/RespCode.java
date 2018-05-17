package com.fl21.cloud.api.sdk;


/**
 * Created by shenli on 2018/1/13.
 */
public enum RespCode {

    /**正常**/
    SUCCESS("1000","ok"),

    /**校验相关**/
    ILLEGAL_IP ("E5001","非法IP"),
    TOKEN_CHECK_FAILURE("E5002", "TOKEN校验失败"),
    TOKEN_TIME_OUT("E5114","token过期"),
    SIGN_CHECK_FAILURE("E5116", "SIGN校验失败"),
    UNSUPPORT_CONTENT_TYPE("E5003","不支持的Contenet-Type"),
    NOT_SUPPORT_VERIFY_FAILURE("E5117","被访问资源校验异常"),
    NOT_ALLOW_ACCESS("E5115", "无权访问该资源"),
    UNKNOW_TOKEN_STATUS("E5118","未知的token状态"),


    /**参数错误*/
    ILLEGAL_PARAMS("E5004", "非法请求参数"),
    ILLEGAL_INST_ID("E5006", "非法的机构ID"),
    ILLEGAL_REPORT_ID("E5011", "非法的报告ID"),
    ILLEGAL_USER_ID("E5008", "非法的用户ID"),
    ILLEGAL_APP_ID("E5116", "非法的应用标识"),



    /**默认错误,未知错误*/
    SERVER_INTERNAL_ERROR("E5109", "服务器内部错误"),
    REQUEST_TOO_FREQUENT("E5113", "请求过于频繁"),

    /***/
    REPORT_NOT_PARSED("E5110", "报告尚未解析完成"),
    REPORT_MD5_CHECK_FAILURE("E5112", "报告上传不完整"),


    /** 网络状态相关的 */

    HTTP_STATUS_400("HTTP-400", "请求参数错误"),
    HTTP_STATUS_401("HTTP-401", "访问被拒绝"),
    HTTP_STATUS_403("HTTP-403", "资源不可用"),
    HTTP_STATUS_404("HTTP-404", "请求的资源不存在"),
    HTTP_STATUS_405("HTTP-405", "请求方法对指定的资源不适用"),
    HTTP_STATUS_406("HTTP-406", "指定的资源MIME类型不匹配"),
    HTTP_STATUS_407("HTTP-407", "要求进行代理身份验证"),
    HTTP_STATUS_408("HTTP-408", "请求超时"),

    HTTP_STATUS_500("HTTP-500", "服务器内部错误"),
    HTTP_STATUS_501("HTTP-501", "服务不支持"),
    HTTP_STATUS_502("HTTP-502", "应用程序出错"),
    HTTP_STATUS_503("HTTP-503", "服务不可用"),
    HTTP_STATUS_504("HTTP-504", "网关超时"),

    ;

    RespCode(String code, String msg){
        this.code = code;
        this.msg = msg;
    }


    private String code;
    private String msg;


    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}


