package com.moodle.tenant;

import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moodle.tenant.model.Request;
import org.junit.Test;
import org.mockito.Mock;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by andrewlarsen on 8/24/17.
 */
public class PostMoodleTenantHandlerTest {

    @Mock
    private Context context;
    private PostMoodleTenantHandler handler = new PostMoodleTenantHandler();

    @Test
    public void testHandler() throws JsonProcessingException {
        Request request = new Request();
        request.setValue("VALUE");
        assertThat(handler.handleRequest(request, context).getBody(),
                is(new ObjectMapper().writeValueAsString(request)));
    }

}