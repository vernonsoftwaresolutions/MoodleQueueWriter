package com.moodle.tenant;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.moodle.tenant.factory.ProxyResponseFactory;
import com.moodle.tenant.lambda.ProxyResponse;
import com.moodle.tenant.model.Request;
import org.apache.http.HttpStatus;

import java.util.logging.Logger;

/**
 * Created by andrewlarsen on 8/24/17.
 */
public class PostMoodleTenantHandler implements RequestHandler<Request, ProxyResponse> {
    private final Logger log = Logger.getLogger(this.getClass().getName());

    private ProxyResponseFactory factory;

    @Override
    public ProxyResponse handleRequest(Request request, Context context) {
        log.info("Received request "+ request);

        return factory.createResponse(request, HttpStatus.SC_ACCEPTED, null);
    }

}
