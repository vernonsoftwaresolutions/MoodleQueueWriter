package com.moodle.tenant.sqs;

import com.amazonaws.services.sqs.model.InvalidMessageContentsException;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.amazonaws.services.sqs.model.UnsupportedOperationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.moodle.tenant.model.MoodleTenantRequest;

/**
 * Created by andrewlarsen on 9/9/17.
 */
public interface SQSClient {

    SendMessageResult sendMessage(MoodleTenantRequest request) throws InvalidMessageContentsException, UnsupportedOperationException, JsonProcessingException;
}
