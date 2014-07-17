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

package com.geomenum.r2d2.spring.servicelayer;

import com.geomenum.r2d2.common.Request;
import com.geomenum.r2d2.servicelayer.RequestHandler;
import com.geomenum.r2d2.servicelayer.RequestHandlerResolutionException;
import com.geomenum.r2d2.servicelayer.ServiceLayerConfiguration;
import com.geomenum.r2d2.spring.ioc.SpringContainer;

import java.util.Map;

public class SpringServiceLayerConfiguration extends ServiceLayerConfiguration {

    public SpringServiceLayerConfiguration(SpringContainer container) {
        super(container);
    }

    @Override
    protected void initializeRequestHandlerTypeRegistry() {
        Map<String, RequestHandler> requestHandlers = getContainer().getBeansOfType(RequestHandler.class);
        for(RequestHandler requestHandler : requestHandlers.values()) {
            registerRequestHandlerType(requestHandler.getClass());
        }
    }

    @Override
    protected Class<? extends RequestHandler> resolveRequestHandlerType(Class<? extends Request> requestType) {
        Map<String, RequestHandler> requestHandlers = getContainer().getBeansOfType(RequestHandler.class);
        for(RequestHandler requestHandler : requestHandlers.values()) {
            if(requestType.equals(resolveHandledRequestType(requestHandler.getClass()))) {
                return requestHandler.getClass();
            }
        }
        throw new RequestHandlerResolutionException("Cannot resolve request handler type for request type " + requestType.getName());
    }

    @Override
    protected SpringContainer getContainer() {
        return (SpringContainer) super.getContainer();
    }
}
