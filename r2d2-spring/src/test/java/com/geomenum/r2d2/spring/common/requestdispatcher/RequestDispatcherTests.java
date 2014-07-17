/*
 * Copyright 2014 Martin Jeannot
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.geomenum.r2d2.spring.common.requestdispatcher;

import com.geomenum.r2d2.common.*;
import com.geomenum.r2d2.spring.config.RRSLTestConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

@ContextConfiguration(classes = {RRSLTestConfiguration.class})
@Test
public class RequestDispatcherTests extends AbstractTestNGSpringContextTests {

    @Autowired
    private RequestDispatcher requestDispatcher;

    @BeforeMethod
    public void setUp() {
        requestDispatcher.clear();
    }

    public void getResponseFromSingleRequest() {
        requestDispatcher.add(new RequestA());

        Response response = requestDispatcher.getResponse(ResponseA.class);
        assertNotNull(response);
        assertEquals(response.getClass(), ResponseA.class);
    }

    public void getResponseFromSingleShot() {
        Response response = requestDispatcher.getResponse(new RequestA(), ResponseA.class);
        assertNotNull(response);
        assertEquals(response.getClass(), ResponseA.class);
    }

    public void getResponseFromMultipleRequests() {
        requestDispatcher.add(new RequestA(), new RequestB());

        Response response1 = requestDispatcher.getResponse(ResponseA.class);
        Response response2 = requestDispatcher.getResponse(ResponseB.class);
        assertNotNull(response1);
        assertNotNull(response2);
        assertEquals(response1.getClass(), ResponseA.class);
        assertEquals(response2.getClass(), ResponseB.class);
    }

    public void getResponseFromKey() {
        requestDispatcher.add("request1", new RequestA());
        requestDispatcher.add("request2", new RequestA());

        Response response1 = requestDispatcher.getResponse("request1");
        Response response2 = requestDispatcher.getResponse("request2");
        assertNotNull(response1);
        assertNotNull(response2);
        assertEquals(response1.getClass(), ResponseA.class);
        assertEquals(response2.getClass(), ResponseA.class);
        assertNotEquals(response1, response2);
    }
}
