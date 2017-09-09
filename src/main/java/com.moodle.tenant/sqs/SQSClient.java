package com.moodle.tenant.sqs;

import com.moodle.tenant.model.Request;

/**
 * Created by andrewlarsen on 9/9/17.
 */
public interface SQSClient {

    void sendMessage(Request request);
}
