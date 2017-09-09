package com.moodle.tenant.model;

/**
 * Created by andrewlarsen on 9/9/17.
 */
public class Request {

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Request{" +
                "value='" + value + '\'' +
                '}';
    }
}
