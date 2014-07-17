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

import com.geomenum.r2d2.common.AbstractRequestDispatcher;
import com.geomenum.r2d2.common.Request;
import com.geomenum.r2d2.common.Response;

import java.util.*;

public class RequestDispatcherStub extends AbstractRequestDispatcher {

    private List<Response> responsesToReturn = new ArrayList<>();
    private Map<String, Request> keyToRequest = new HashMap<>();

    public RequestDispatcherStub() {
        super(null);
    }

    public void addResponseToReturn(Response response) {
        responsesToReturn.add(response);
    }

    public void addResponseToReturn(String key, Response response) {
        responsesToReturn.add(response);
        addToKeyToResultPositionsForTest(key, getKeyToResultPositionsSize() - 1);
    }

    public void addResponseToReturn(Response... responses) {
        responsesToReturn.addAll(Arrays.asList(responses));
    }

    public void addResponseToReturn(Map<String, Response> keyedResponses) {
        responsesToReturn.addAll(keyedResponses.values());

        int i = 0;
        for(String key : keyedResponses.keySet()) {
            if(key != null) {
                addToKeyToResultPositionsForTest(key, i++);
            }
        }
    }

    @Override
    public void clear()
    {
        super.clear();
        responsesToReturn = new ArrayList<>();
        keyToRequest = new HashMap<>();
    }

    @Override
    public void add(String key, Request request) {
        super.add(key, request);
        keyToRequest.put(key, request);
    }

    public <REQ extends Request> REQ getRequest(Class<REQ> requestType) {
        for(Request request : getRequests()) {
            if(request.getClass().equals(requestType)) {
                return (REQ) request;
            }
        }
        return null;
    }

    public <REQ extends Request> REQ getRequest(String key) {
        return (REQ) keyToRequest.get(key);
    }

    public <REQ extends Request> boolean hasRequest(Class<REQ> requestType) {
        for(Request request : getRequests()) {
            if(request.getClass().equals(requestType)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected Response[] getResponses(Request... requestsToProcess) {
        return responsesToReturn.toArray(new Response[responsesToReturn.size()]);
    }
}
