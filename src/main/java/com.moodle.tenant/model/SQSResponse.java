package com.moodle.tenant.model;

/**
 * Created by andrewlarsen on 9/9/17.
 */
public class SQSResponse {
    private String messageId;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
}
