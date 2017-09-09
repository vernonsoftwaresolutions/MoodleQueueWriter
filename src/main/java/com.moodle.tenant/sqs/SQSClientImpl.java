package com.moodle.tenant.sqs;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.AmazonSQSException;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.moodle.tenant.model.Request;

/**
 * Created by andrewlarsen on 9/9/17.
 */
public class SQSClientImpl implements SQSClient {
//    AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
//    CreateQueueRequest create_request = new CreateQueueRequest(QUEUE_NAME)
//            .addAttributesEntry("DelaySeconds", "60")
//            .addAttributesEntry("MessageRetentionPeriod", "86400");
//
//

    @Override
    public void sendMessage(Request request) {
//
//        try {
//            sqs.createQueue(create_request);
//        } catch (AmazonSQSException e) {
//            if (!e.getErrorCode().equals("QueueAlreadyExists")) {
//                throw e;
//            }
//        }

    }
}
