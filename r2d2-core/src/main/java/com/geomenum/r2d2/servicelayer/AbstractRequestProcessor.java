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

package com.geomenum.r2d2.servicelayer;

import com.geomenum.r2d2.common.ExceptionType;
import com.geomenum.r2d2.common.Request;
import com.geomenum.r2d2.common.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractRequestProcessor implements RequestProcessor {

    private final ServiceLayerConfiguration serviceLayerConfiguration;
    private static final Logger logger = LoggerFactory.getLogger(AbstractRequestProcessor.class);

    public AbstractRequestProcessor(ServiceLayerConfiguration serviceLayerConfiguration) {
        this.serviceLayerConfiguration = serviceLayerConfiguration;
    }

    @Override
    public Response[] process(Request... requests) {
        if(requests == null) {
            return null;
        }
        List<Response> responses = new ArrayList<>(requests.length);
        boolean exceptionPreviouslyOccurred = false;
        for(Request request : requests) {
            RequestHandler requestHandler = resolveHandler(request);
            try {
                Response response;
                if(!exceptionPreviouslyOccurred) {
                    response = getResponseFromHandler(request, requestHandler);
                    exceptionPreviouslyOccurred = response.hasExceptionOccurred();
                } else {
                    response = requestHandler.createDefaultResponse();
                    response.setExceptionType(ExceptionType.EARLIER_REQUEST_ALREADY_FAILED);
                    response.setException(new Exception(ExceptionType.EARLIER_REQUEST_ALREADY_FAILED.toString()));
                }
                responses.add(response);
            } catch (Exception e) {
                logger.error("An exception occurred while collecting responses", e);
            }
        }
        return responses.toArray(new Response[responses.size()]);
    }

    private RequestHandler resolveHandler(Request request) {
        return serviceLayerConfiguration.resolveRequestHandler(request.getClass());
    }

    private Response getResponseFromHandler(Request request, RequestHandler requestHandler) {
        try {
            beforeHandle(request);
            Response response = requestHandler.handleRequest(request);
            afterHandle(request);
            return response;
        } catch (Exception e) {
            logger.error("Unhandled exception while handling request of type {}", request.getClass().getName(), e);
            Response response = requestHandler.createDefaultResponse();
            response.setException(e);
            response.setExceptionType(getExceptionType(e));
            return response;
        }
    }

    protected ExceptionType getExceptionType(Exception e) {
        // default implementation
        return ExceptionType.UNKNOWN;
    }

    protected void beforeHandle(Request request) {}
    protected void afterHandle(Request request) {}
}
