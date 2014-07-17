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

import com.geomenum.r2d2.common.*;
import org.testng.annotations.Test;

import java.util.Iterator;

import static org.testng.Assert.*;

@Test
public class ProcessRequestsTests {

    private RequestDispatcher requestDispatcher = new RequestDispatcherStub();

    @Test(enabled = false)
    public void doMe() {
        assertTrue(false);
    }

    public void clear() {
        Request request1 = new RequestA(), request2 = new RequestB();
        requestDispatcher.add(request1);
        ((RequestDispatcherStub) requestDispatcher).addResponseToReturn(new ResponseA());
        requestDispatcher.add("myRequest", request2);
        ((RequestDispatcherStub) requestDispatcher).addResponseToReturn("myRequest", new ResponseB());

        assertEquals(getResponsesSize(requestDispatcher), 2);
        requestDispatcher.clear();
        assertEquals(getResponsesSize(requestDispatcher), 0);
        assertFalse(((RequestDispatcherStub) requestDispatcher).hasRequest(RequestA.class));
        assertNull(((RequestDispatcherStub) requestDispatcher).getRequest("myRequest"));
    }

    private int getResponsesSize(RequestDispatcher requestDispatcher) {
        int size = 0;
        Iterator<Response> iterator = requestDispatcher.getResponses().iterator();
        while(iterator.hasNext()) {
            iterator.next();
            size++;
        }
        return size;
    }
}
