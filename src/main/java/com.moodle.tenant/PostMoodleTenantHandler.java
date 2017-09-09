package com.moodle.tenant;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moodle.tenant.factory.ProxyRequestFactory;
import com.moodle.tenant.factory.ProxyResponseFactory;
import com.moodle.tenant.lambda.ProxyRequest;
import com.moodle.tenant.lambda.ProxyResponse;
import com.moodle.tenant.model.Request;
import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * Created by andrewlarsen on 8/24/17.
 */
public class PostMoodleTenantHandler implements RequestHandler<ProxyRequest, ProxyResponse> {
    private final Logger log = Logger.getLogger(this.getClass().getName());

    private ProxyResponseFactory factory;
    private ProxyRequestFactory requestFactory;

    public PostMoodleTenantHandler() {
        ObjectMapper mapper = new ObjectMapper();
        this.factory = new ProxyResponseFactory(mapper);
        this.requestFactory = new ProxyRequestFactory(mapper);
    }

    @Override
    public ProxyResponse handleRequest(ProxyRequest proxyRequest, Context context) {
        Request request = null;
        try {
            request = requestFactory.getBody(proxyRequest.getBody());
            log.info("Received request " + request);

            return factory.createResponse(request, HttpStatus.SC_ACCEPTED, null);
        } catch (IOException e) {
            log.error("Error processing input request");
            return factory.createErrorResponse(500, 500, "Internal Server Error", null);
        }

    }

}
