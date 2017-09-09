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

/**
 * Created by andrewlarsen on 8/24/17.
 */
public class PostMoodleTenantHandler implements RequestHandler<ProxyRequest, ProxyResponse> {
    private final Logger log = Logger.getLogger(this.getClass().getName());

    private String queueName = "awseb-e-vnw39gwxyf-stack-AWSEBWorkerQueue-NUPJYRG24EEX";
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
        try {
            request = requestFactory.getBody(proxyRequest.getBody());

            log.info("Received request " + request);

            SendMessageResult result = sqsClient.sendMessage(request);


            SQSResponse response = new SQSResponse();

            response.setMessageId(result.getMessageId());

            ProxyResponse proxyResponse = factory.createResponse(response, HttpStatus.SC_ACCEPTED, null);

            return proxyResponse;
        }
        catch (UnsupportedOperationException | InvalidMessageContentsException e){
            log.error("Error posting message to sqs queue");
            return factory.createErrorResponse(500, 500, "Error posting message", null);
        }
        catch (IOException e) {
            log.error("Error processing input request");
            return factory.createErrorResponse(500, 500, "Internal Server Error", null);
        }

    }

}
