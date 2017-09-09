package com.moodle.tenant.sqs;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.*;
import com.amazonaws.services.sqs.model.UnsupportedOperationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moodle.tenant.model.MoodleTenantRequest;


/**
 * Created by andrewlarsen on 9/9/17.
 */
public class SQSClientImpl implements SQSClient {

    private AmazonSQS sqs;
    private String queueUrl;
    private ObjectMapper objectMapper;
    public SQSClientImpl(ObjectMapper objectMapper, String queueName) {
        this.objectMapper = objectMapper;
        this.queueUrl = sqs.getQueueUrl(queueName).getQueueUrl();
        sqs = AmazonSQSClientBuilder.defaultClient();

    }

    @Override
    public SendMessageResult sendMessage(MoodleTenantRequest request) throws InvalidMessageContentsException,
            UnsupportedOperationException, JsonProcessingException {

        SendMessageRequest send_msg_request = new SendMessageRequest()
                .withQueueUrl(queueUrl)
                .withMessageBody(objectMapper.writeValueAsString(request))
                .withDelaySeconds(5);

        return sqs.sendMessage(send_msg_request);


    }
}
