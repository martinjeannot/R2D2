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

package com.geomenum.r2d2.common.requestdispatcher;

import com.geomenum.r2d2.common.Request;
import com.geomenum.r2d2.common.RequestA;
import com.geomenum.r2d2.common.RequestB;
import com.geomenum.r2d2.common.RequestDispatcher;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

@Test
public class AddRequestTests {

    private RequestDispatcher requestDispatcher = new RequestDispatcherStub();

    @BeforeMethod
    public void setUp() {
        requestDispatcher.clear();
    }

    public void addRequest() {
        Request request = new RequestA();
        requestDispatcher.add(request);

        assertTrue(((RequestDispatcherStub) requestDispatcher).hasRequest(RequestA.class));
        assertEquals(((RequestDispatcherStub) requestDispatcher).getRequest(RequestA.class), request);
    }

    public void addMultipleRequests() {
        Request request1 = new RequestA(), request2 = new RequestB();
        requestDispatcher.add(request1, request2);

        assertTrue(((RequestDispatcherStub) requestDispatcher).hasRequest(RequestA.class));
        assertEquals(((RequestDispatcherStub) requestDispatcher).getRequest(RequestA.class), request1);
        assertTrue(((RequestDispatcherStub) requestDispatcher).hasRequest(RequestB.class));
        assertEquals(((RequestDispatcherStub) requestDispatcher).getRequest(RequestB.class), request2);
    }

    public void addRequestWithKey() {
        Request request = new RequestA();
        requestDispatcher.add("myRequest", request);

        assertTrue(((RequestDispatcherStub) requestDispatcher).hasRequest(RequestA.class));
        assertEquals(((RequestDispatcherStub) requestDispatcher).getRequest("myRequest"), request);
    }

    @Test(expectedExceptions = {IllegalArgumentException.class},
            expectedExceptionsMessageRegExp = "A request of type com.geomenum.r2d2.common.RequestA has already been"
                    + " added. Please add requests of the same type with a different key.")
    public void throwsExceptionWhenDoubleAddingSameRequestType() {
        Request request1 = new RequestA(), request2 = new RequestA();
        requestDispatcher.add(request1);

        requestDispatcher.add(request2);
    }

    @Test(expectedExceptions = {IllegalArgumentException.class},
            expectedExceptionsMessageRegExp = "A request of type com.geomenum.r2d2.common.RequestA has already been"
                    + " added. Please add requests of the same type with a different key.")
    public void throwsExceptionWhenDoubleAddingSameRequestTypeInArrayAddition() {
        Request request1 = new RequestA(), request2 = new RequestB(), request3 = new RequestA();

        requestDispatcher.add(request1, request2, request3);
    }

    @Test(expectedExceptions = {IllegalArgumentException.class},
            expectedExceptionsMessageRegExp = "A request has already been added using the key : myRequest")
    public void throwsExceptionWhenDoubleAddingSameRequestKey() {
        Request request1 = new RequestA(), request2 = new RequestB();
        requestDispatcher.add("myRequest", request1);

        requestDispatcher.add("myRequest", request2);
    }
}
