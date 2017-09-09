package com.moodle.tenant.factory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moodle.tenant.model.Request;


import java.io.IOException;

/**
 * Created by andrewlarsen on 9/9/17.
 */
public class ProxyRequestFactory {
    private ObjectMapper objectMapper;

    public ProxyRequestFactory(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public Request getBody(String content) throws IOException {
        return objectMapper.readValue(content, Request.class);
    }
}
