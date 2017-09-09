package com.moodle.tenant;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.sqs.model.InvalidMessageContentsException;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moodle.tenant.factory.ProxyRequestFactory;
import com.moodle.tenant.factory.ProxyResponseFactory;
import com.moodle.tenant.lambda.ErrorPayload;
import com.moodle.tenant.lambda.ProxyRequest;
import com.moodle.tenant.lambda.ProxyResponse;
import com.moodle.tenant.model.MoodleTenantRequest;
import com.moodle.tenant.model.SQSResponse;
import com.moodle.tenant.sqs.SQSClient;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;

/**
 * Created by andrewlarsen on 8/24/17.
 */
public class PostMoodleTenantHandlerTest {

    @Mock
    private Context context;
    @Mock
    private SQSClient client;
    @Mock
    private ProxyRequestFactory requestFactory;
    @Mock
    private ProxyResponseFactory responseFactory;

    private ProxyResponse response;
    private ObjectMapper objectMapper = new ObjectMapper();
    private MoodleTenantRequest request;
    private PostMoodleTenantHandler handler;
    private ProxyRequest proxyRequest;
    private SendMessageResult result;
    private SQSResponse sqsResponse;

    @Before
    public void setup() throws JsonProcessingException {
        MockitoAnnotations.initMocks(this);
        proxyRequest = new ProxyRequest();
        response = new ProxyResponse();
        sqsResponse = new SQSResponse();

        request = new MoodleTenantRequest();
        request.setClientName("CLIENTNAME");
        request.setHostedZoneName("HOSTEDZONENAME");
        request.setPriority(1);
        request.setStackName("STACKNAME");
        request.setVpcId("VPCID");
        result = new SendMessageResult().withMessageId("MESSAGEID");

        sqsResponse.setMessageId(result.getMessageId());

        proxyRequest.setBody(objectMapper.writeValueAsString(request));

        response.setBody(objectMapper.writeValueAsString(sqsResponse));

        handler = new PostMoodleTenantHandler("QUEUE",responseFactory, requestFactory,client);
    }
    @Test
    public void testHandler() throws IOException {

        proxyRequest.setBody(new ObjectMapper().writeValueAsString(request));

        given(requestFactory.getBody(anyString())).willReturn(new MoodleTenantRequest());

        given(client.sendMessage(anyObject())).willReturn(result);

        response.setStatusCode(200);
        response.setHeaders(null);
        given(responseFactory.createResponse(anyObject(), eq(HttpStatus.SC_ACCEPTED), eq(null))).willReturn(response);

        ProxyResponse expected = handler.handleRequest(proxyRequest, context);

        assertThat(objectMapper.writeValueAsString(expected), is(objectMapper.writeValueAsString(response)));
    }
    @Test
    public void testHandlerUnsupportedOperationException() throws IOException {

        proxyRequest.setBody(new ObjectMapper().writeValueAsString(request));

        given(requestFactory.getBody(anyString())).willReturn(new MoodleTenantRequest());

        given(client.sendMessage(anyObject())).willThrow(new UnsupportedOperationException());

        ErrorPayload error = new ErrorPayload(500, "Error posting message");

        response.setStatusCode(500);
        response.setHeaders(null);
        response.setBody(objectMapper.writeValueAsString(error));

        given(responseFactory.createErrorResponse(500, 500,
                "Error posting message", null)).willReturn(response);

        ProxyResponse expected = handler.handleRequest(proxyRequest, context);

        assertThat(objectMapper.writeValueAsString(expected), is(objectMapper.writeValueAsString(response)));
    }
    @Test
    public void testHandlerInvalidMessageContentsException() throws IOException {

        proxyRequest.setBody(new ObjectMapper().writeValueAsString(request));

        given(requestFactory.getBody(anyString())).willReturn(new MoodleTenantRequest());

        given(client.sendMessage(anyObject())).willThrow(new InvalidMessageContentsException("MESSAGE"));

        ErrorPayload error = new ErrorPayload(500, "Error posting message");

        response.setStatusCode(500);
        response.setHeaders(null);
        response.setBody(objectMapper.writeValueAsString(error));

        given(responseFactory.createErrorResponse(500, 500,
                "Error posting message", null)).willReturn(response);

        ProxyResponse expected = handler.handleRequest(proxyRequest, context);

        assertThat(objectMapper.writeValueAsString(expected), is(objectMapper.writeValueAsString(response)));
    }
    @Test
    public void testHandlerIOException() throws IOException {

        proxyRequest.setBody("NOTJSON");

        given(requestFactory.getBody(anyString())).willThrow(new IOException());

        given(client.sendMessage(anyObject())).willThrow(new InvalidMessageContentsException("MESSAGE"));

        ErrorPayload error = new ErrorPayload(500, "Error posting message");

        response.setStatusCode(500);
        response.setHeaders(null);
        response.setBody(objectMapper.writeValueAsString(error));

        given(responseFactory.createErrorResponse(500, 500,
                "Internal Server Error", null)).willReturn(response);

        ProxyResponse expected = handler.handleRequest(proxyRequest, context);

        assertThat(objectMapper.writeValueAsString(expected), is(objectMapper.writeValueAsString(response)));
    }
}