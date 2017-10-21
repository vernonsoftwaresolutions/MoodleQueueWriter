package com.moodle.tenant;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.sqs.model.InvalidMessageContentsException;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moodle.tenant.factory.ProxyRequestFactory;
import com.moodle.tenant.factory.ProxyResponseFactory;
import com.moodle.tenant.lambda.ProxyRequest;
import com.moodle.tenant.lambda.ProxyResponse;
import com.moodle.tenant.model.MoodleTenantRequest;
import com.moodle.tenant.model.SQSResponse;
import com.moodle.tenant.sqs.SQSClient;
import com.moodle.tenant.sqs.SQSClientImpl;
import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by andrewlarsen on 8/24/17.
 */
public class PostMoodleTenantHandler implements RequestHandler<ProxyRequest, ProxyResponse> {
    private final Logger log = Logger.getLogger(this.getClass().getName());
    //todo- refactor app to not need to hardcode this value.  Should use naming convention instead
    private String queueName = "awseb-e-422piwbzkd-stack-AWSEBWorkerQueue-RYEQU7Y64XZO";
    private ProxyResponseFactory factory;
    private ProxyRequestFactory requestFactory;
    private SQSClient sqsClient;

    public PostMoodleTenantHandler() {
        ObjectMapper mapper = new ObjectMapper();
        this.factory = new ProxyResponseFactory(mapper);
        this.requestFactory = new ProxyRequestFactory(mapper);
        this.sqsClient = new SQSClientImpl(mapper, queueName);
    }

    public PostMoodleTenantHandler(String queueName, ProxyResponseFactory factory, ProxyRequestFactory requestFactory, SQSClient sqsClient) {
        this.queueName = queueName;
        this.factory = factory;
        this.requestFactory = requestFactory;
        this.sqsClient = sqsClient;
    }

    @Override
    public ProxyResponse handleRequest(ProxyRequest proxyRequest, Context context) {
        MoodleTenantRequest request = null;
        log.info("Received request " + proxyRequest);

        try {
            request = requestFactory.getBody(proxyRequest.getBody());

            log.info("created request " +  request);
            SendMessageResult result = sqsClient.sendMessage(request);

            log.info("SQS response " + result);

            SQSResponse response = new SQSResponse();

            response.setMessageId(result.getMessageId());

            ProxyResponse proxyResponse = factory.createResponse(response, HttpStatus.SC_ACCEPTED, createCorsHeaders());
            log.info("About to return proxy response  " + proxyResponse );

            return proxyResponse;
        }
        catch (UnsupportedOperationException | InvalidMessageContentsException e){
            log.error("Error posting message to sqs queue", e);
            return factory.createErrorResponse(500, 500, "Error posting message", createCorsHeaders());
        }
        catch (IOException e) {
            log.error("Error processing input request", e);
            return factory.createErrorResponse(500, 500, "Internal Server Error", createCorsHeaders());
        }

    }

    /**
     * Helper method to create cors headers for Browsers
     * @return
     */
    private Map<String, String> createCorsHeaders(){
        Map headers = new HashMap<String, String>();
        headers.put( "Access-Control-Allow-Origin", "*");
        return headers;
    }

}
