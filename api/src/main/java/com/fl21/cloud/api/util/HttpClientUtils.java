package com.fl21.cloud.api.util;


import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;


public class HttpClientUtils {


    public void post(String postUrl, Map<String,String> headMap,
                         HttpEntityProvider provider,
                         HttpEntityResponseHandler handler){
        CloseableHttpClient httpclient = null;
        CloseableHttpResponse response = null;
        HttpEntity respEntity;
        try {
            httpclient = HttpClients.createDefault();
            HttpPost post = new HttpPost(postUrl);
            if(headMap != null && !headMap.isEmpty()) {
                addHeader(headMap, post);
            }
            HttpEntity httpEntity = provider.getHttpEntity();
            post.setEntity(httpEntity);
            response = httpclient.execute(post);
            respEntity = response.getEntity();
            handler.handler(respEntity);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CommonUtils.closeResponse(response);
            CommonUtils.closeHttpClient(httpclient);
        }
    }

    private void addHeader(Map<String, String> headMap,HttpPost post) {
        for (Map.Entry<String, String> entry : headMap.entrySet()) {
            post.addHeader(entry.getKey(), entry.getValue());
        }
    }

    public String getEntityString(HttpEntity httpEntity, String charset) {
        try {
            Charset charset1 = charset == null ? Charset.forName("UTF8") : Charset.forName(charset);
            return EntityUtils.toString(httpEntity, charset1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void get(String getUrl, Map<String,String> headMap,Map<String,String> getParams,HttpEntityResponseHandler handler) {
        CloseableHttpClient httpclient = null;
        CloseableHttpResponse response = null;
        HttpEntity respEntity;
        try {
            httpclient = HttpClients.createDefault();
            String queryStr = getQueryStr(getParams);
            HttpGet httpGet = new HttpGet(getUrl+ queryStr);
            if (!CollectionUtils.isEmpty(headMap)) {
                httpGet.setHeaders(transMap2Header(headMap));
            }
            response = httpclient.execute(httpGet);
            respEntity = response.getEntity();
            handler.handler(respEntity);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            CommonUtils.closeResponse(response);
            CommonUtils.closeHttpClient(httpclient);
        }
    }

    public void postFile(String postUrl, Map<String,String> headMap,
                         Map<String,String> formMap,File file,
                         HttpEntityResponseHandler handler){
        CloseableHttpClient httpclient = null;
        CloseableHttpResponse response = null;
        HttpEntity respEntity = null;
        try {
            httpclient = HttpClients.createDefault();
            HttpPost post = new HttpPost(postUrl);
            if(!CollectionUtils.isEmpty(headMap)) {
                post.setHeaders(transMap2Header(headMap));
            }
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setCharset(Charset.forName("utf-8"));
            builder.setMode(HttpMultipartMode.RFC6532);
            if (!CollectionUtils.isEmpty(formMap)) {
                for (Map.Entry<String, String> entry : formMap.entrySet()) {
                    builder.addTextBody(entry.getKey(), entry.getValue(), ContentType.TEXT_PLAIN.withCharset("UTF-8"));
                }
            }
            if (file.exists()) {
                builder.addBinaryBody("file", file);
            }
            HttpEntity httpEntity = builder.build();
            post.setEntity(httpEntity);
            response = httpclient.execute(post);
            respEntity = response.getEntity();
            handler.handler(respEntity);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CommonUtils.closeResponse(response);
            CommonUtils.closeHttpClient(httpclient);
        }
    }



    public String getEntityString(HttpEntity httpEntity) {
        return getEntityString(httpEntity, null);
    }



    public String getPostStr(Map<String, String> postParams) {
        StringBuilder s = new StringBuilder();
        if(postParams != null && !postParams.isEmpty()) {
            append(s, postParams);
            s.delete(s.length() - 1, s.length());
        }
        return s.toString();
    }

    private void append(StringBuilder s, Map<String, String> map) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String val = entry.getValue();
            if (val != null && !"".equals(val)) {
                s.append(key).append("=").append(val).append("&");
            }
        }
    }

    public String getQueryStr(Map<String, String> getParams) {
        StringBuilder s = new StringBuilder();
        if(!CollectionUtils.isEmpty(getParams)) {
            s.append("?");
            append(s, getParams);
            s.delete(s.length() - 1, s.length());
        }
        return s.toString();
    }

    private Header[] transMap2Header(Map<String,String> headMap){
        Header[] headers = new Header[headMap.size()];
        int i = 0;
        for(Map.Entry<String,String> entry :headMap.entrySet()){
            headers[i] = new BasicHeader(entry.getKey(), entry.getValue());
            i++;
        }
        return headers;
    }



}
