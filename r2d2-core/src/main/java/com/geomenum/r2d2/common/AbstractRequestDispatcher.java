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

package com.geomenum.r2d2.common;

import com.geomenum.r2d2.servicelayer.RequestProcessor;

import java.util.*;

public abstract class AbstractRequestDispatcher implements RequestDispatcher {

    private final RequestProcessor requestProcessor;

    private List<Request> requests;
    private Response[] responses;
    private Map<String, Class> keyToTypes;
    private Map<String, Integer> keyToResultPositions;

    public AbstractRequestDispatcher(RequestProcessor requestProcessor) {
        this.requestProcessor = requestProcessor;
        initializeState();
    }

    private void initializeState() {
        requests = new ArrayList<>();
        responses = null;
        keyToTypes = new HashMap<>();
        keyToResultPositions = new HashMap<>();
    }

    @Override
    public void clear() {
        initializeState();
    }

    //~ Requests =======================================================================================================

    @Override
    public void add(Request request) {
        addRequest(request, false);
    }

    @Override
    public void add(Request... requests) {
        for(Request request : requests) {
            add(request);
        }
    }

    @Override
    public void add(String key, Request request) {
        if(keyToTypes.containsKey(key)) {
            throw new IllegalArgumentException("A request has already been added using the key : " + key);
        }
        keyToTypes.put(key, request.getClass());
        addRequest(request, true);
        keyToResultPositions.put(key, requests.size() - 1);
    }

    private void addRequest(Request request, boolean wasAddedWithKey) {
        if(requestsSent()) {
            throw new IllegalStateException("Requests were already sent. Either add request earlier or call clear().");
        }
        Class requestType = request.getClass();
        if(requestTypeIsAlreadyPresent(requestType)
                && (requestTypeIsNotAssociatedWithKey(requestType) || !wasAddedWithKey)) {
            throw new IllegalArgumentException("A request of type " + requestType.getName() + " has already been added. " +
                    "Please add requests of the same type with a different key.");
        }
        requests.add(request);
    }

    private boolean requestsSent() {
        return responses != null;
    }

    private boolean requestTypeIsAlreadyPresent(Class requestType) {
        for(Request request : requests) {
            if(request.getClass().equals(requestType)) {
                return true;
            }
        }
        return false;
    }

    private boolean requestTypeIsNotAssociatedWithKey(Class requestType) {
        return !keyToTypes.values().contains(requestType);
    }

    //~ Responses ======================================================================================================

    @Override
    public Iterable<Response> getResponses() {
        sendRequestsIfNecessary();
        return Arrays.asList(responses);
    }

    @Override
    public <RES extends Response> boolean hasResponse(Class<RES> responseType) {
        sendRequestsIfNecessary();
        return !getResponsesOfType(responseType).isEmpty();
    }

    @Override
    public <RES extends Response> RES getResponse(Class<RES> responseType) {
        sendRequestsIfNecessary();
        if(!hasResponse(responseType)) {
            throw new IllegalArgumentException("There is no response of type " + responseType.getName() + ". " +
                    "Maybe you called clear() before or forgot to add appropriate request first.");
        }
        if(hasMoreThanOneResponse(responseType)) {
            throw new IllegalArgumentException("There is more than one response of type " + responseType.getName() + ". " +
                    "If two request handlers return responses with the same type, you need to add requests using add(String key, Request request).");
        }
        return getResponsesOfType(responseType).iterator().next();
    }

    @Override
    public <RES extends Response> RES getResponse(String key) {
        sendRequestsIfNecessary();
        if(!hasResponse(key)) {
            throw new IllegalArgumentException("There is no response with key '" + key + "'. " +
                    "Maybe you called clear() before or forgot to add appropriate request first.");
        }
        return (RES) responses[keyToResultPositions.get(key)];
    }

    @Override
    public <RES extends Response> RES getResponse(Request request, Class<RES> responseType) {
        add(request);
        return getResponse(responseType);
    }

    private <RES extends Response> boolean hasMoreThanOneResponse(Class<RES> responseType) {
        sendRequestsIfNecessary();
        return getResponsesOfType(responseType).size() > 1;
    }

    private boolean hasResponse(String key) {
        sendRequestsIfNecessary();
        return keyToResultPositions.containsKey(key);
    }

    private <RES extends Response> Collection<RES> getResponsesOfType(Class<RES> responseType) {
        sendRequestsIfNecessary();
        List<RES> responsesOfRequestedType = new ArrayList<>();
        for(Response response : responses) {
            if(response.getClass().equals(responseType)) {
                responsesOfRequestedType.add((RES) response);
            }
        }
        return responsesOfRequestedType;
    }

    //~ Processing =====================================================================================================

    private void sendRequestsIfNecessary() {
        if(!requestsSent()) {
            responses = getResponses(requests.toArray(new Request[requests.size()]));
            dealWithPossibleExceptions(Arrays.asList(responses));
        }
    }

    protected Response[] getResponses(Request... requestsToProcess) {
        beforeSendingRequests(Arrays.asList(requestsToProcess));

        Response[] tempResponseArray = new Response[requestsToProcess.length];
        List<Request> requestsToSend = Arrays.asList(requestsToProcess);

        getCachedResponsesAndRemoveThoseRequests(requestsToProcess, tempResponseArray, requestsToSend);
        Request[] requestsToSendArray = requestsToSend.toArray(new Request[requestsToSend.size()]);

        if(requestsToSendArray.length > 0) {
            Response[] receivedResponses = requestProcessor.process(requestsToSendArray);
            addCacheableResponsesToCache(receivedResponses, requestsToSendArray);
            putReceivedResponsesInTempResponseArray(tempResponseArray, receivedResponses);
        }

        afterSendingRequests(Arrays.asList(requestsToProcess));
        beforeReturningResponses(Arrays.asList(tempResponseArray));
        return tempResponseArray;
    }

    private void getCachedResponsesAndRemoveThoseRequests(Request[] requestsToProcess, Response[] tempResponseArray, List<Request> requestsToSend) {
        // todo
    }

    private void addCacheableResponsesToCache(Response[] receivedResponses, Request[] requestsToSend) {
        // todo
    }

    private void putReceivedResponsesInTempResponseArray(Response[] tempResponseArray, Response[] receivedResponses) {
        System.arraycopy(receivedResponses, 0, tempResponseArray, 0, receivedResponses.length);
    }

    protected void beforeSendingRequests(Iterable<Request> requests) {}
    protected void afterSendingRequests(Iterable<Request> requests) {}
    protected void beforeReturningResponses(Iterable<Response> responses) {}

    private void dealWithPossibleExceptions(Iterable<Response> responsesToCheck) {
        for(Response response : responsesToCheck) {
            switch(response.getExceptionType()) {
                case SECURITY: dealWithSecurityException(response.getException());
                    break;
                case BUSINESS: dealWithBusinessException(response.getException());
                    break;
                case PERSISTENCE: dealWithPersistenceException(response.getException());
                    break;
                case UNKNOWN: dealWithUnknownException(response.getException());
                    break;
            }
        }
    }

    protected void dealWithSecurityException(Exception exception) {}
    protected void dealWithBusinessException(Exception exception) {}
    protected void dealWithPersistenceException(Exception exception) {}
    protected void dealWithUnknownException(Exception exception) {}

    //~ Testing ========================================================================================================

    protected void addToKeyToResultPositionsForTest(String key, Integer position) {
        keyToResultPositions.put(key, position);
    }

    protected int getKeyToResultPositionsSize() {
        return keyToResultPositions.size();
    }

    protected Iterable<Request> getRequests() {
        return Collections.unmodifiableCollection(requests);
    }
}
