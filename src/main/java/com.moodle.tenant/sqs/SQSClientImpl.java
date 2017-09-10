package com.moodle.tenant.sqs;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.*;
import com.amazonaws.services.sqs.model.UnsupportedOperationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moodle.tenant.model.MoodleTenantRequest;
import org.apache.log4j.Logger;


/**
 * Created by andrewlarsen on 9/9/17.
 */
public class SQSClientImpl implements SQSClient {
    private final Logger log = Logger.getLogger(this.getClass().getName());

    private AmazonSQS sqs;
    private String queueUrl;
    private ObjectMapper objectMapper;
    public SQSClientImpl(ObjectMapper objectMapper, String queueName) {
        this.objectMapper = objectMapper;
        sqs = AmazonSQSClientBuilder.defaultClient();
        this.queueUrl = sqs.getQueueUrl(queueName).getQueueUrl();

    }

    @Override
    public SendMessageResult sendMessage(MoodleTenantRequest request) throws InvalidMessageContentsException,
            UnsupportedOperationException, JsonProcessingException {
        log.info("Creating ");
        SendMessageRequest send_msg_request = new SendMessageRequest()
                .withQueueUrl(queueUrl)
                .withMessageBody(objectMapper.writeValueAsString(request))
                .withDelaySeconds(5);
        log.info("About to send message " + send_msg_request);

        return sqs.sendMessage(send_msg_request);


    }
}
