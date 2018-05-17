package com.fl21.cloud.api.util;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;


public class CommonUtils {
    public static void closeResponse(CloseableHttpResponse response) {
        try {
            if (response != null) {
                response.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void closeHttpClient(CloseableHttpClient httpclient) {
        try {
            if (httpclient != null) {
                httpclient.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
