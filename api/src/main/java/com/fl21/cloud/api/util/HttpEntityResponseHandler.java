package com.fl21.cloud.api.util;

import org.apache.http.HttpEntity;

public interface HttpEntityResponseHandler {

    void handler(HttpEntity responseEntity);
}
