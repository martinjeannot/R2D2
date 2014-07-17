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

import com.geomenum.r2d2.common.Request;
import com.geomenum.r2d2.ioc.Container;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;

public abstract class ServiceLayerConfiguration {

    private final Container container;
    private final Map<Class<? extends Request>, Class<? extends RequestHandler>> requestHandlerTypeRegistry;

    public ServiceLayerConfiguration(Container container) {
        this.container = container;
        this.requestHandlerTypeRegistry = new HashMap<>();
        init();
    }

    private void init() {
        initializeRequestHandlerTypeRegistry();
    }

    protected Container getContainer() {
        return container;
    }

    //~ Request handlers registration process ==========================================================================

    protected void initializeRequestHandlerTypeRegistry() {}

    protected void registerRequestHandlerType(Class<? extends RequestHandler> requestHandlerType) {
        registerRequestHandlerType(resolveHandledRequestType(requestHandlerType), requestHandlerType);
    }

    private void registerRequestHandlerType(Class<? extends Request> requestType, Class<? extends RequestHandler> requestHandlerType) {
        if(requestType == null || requestHandlerType == null) {
            throw new NullPointerException("Cannot register a request handler with "
                    + (requestType == null ? "null request type." : "null request handler type."));
        }
        if(requestHandlerTypeRegistry.containsKey(requestType)) {
            throw new IllegalArgumentException(String.format("Found two request handlers that handle the same request type : %s\n"
                    + "First request handler  : %s\n"
                    + "Second request handler : %s\n"
                    + "For each request type there must be only one request handler.",
                    requestType.getName(), requestHandlerTypeRegistry.get(requestType).getName(), requestHandlerType.getName()));
        }
        requestHandlerTypeRegistry.put(requestType, requestHandlerType);
    }

    protected final Class<? extends Request> resolveHandledRequestType(Class<? extends RequestHandler> requestHandlerType) {
        Type genericSuperclass = requestHandlerType.getGenericSuperclass();
        while(!(genericSuperclass instanceof ParameterizedType)) {
            genericSuperclass = ((Class) genericSuperclass).getGenericSuperclass();
        }
        Type requestType = ((ParameterizedType) genericSuperclass).getActualTypeArguments()[0];
        if(requestType instanceof Class) {
            return (Class<? extends Request>) requestType;
        } else if(requestType instanceof TypeVariable) {
            Type firstBound = ((TypeVariable) requestType).getBounds()[0];
            if(firstBound instanceof Class) {
                return (Class<? extends Request>) firstBound;
            }
        }
        throw new RequestHandlerResolutionException("Cannot resolve handled request type for handler type " + requestHandlerType.getName());
    }

    //~ Request handlers resolution process ============================================================================

    abstract protected Class<? extends RequestHandler> resolveRequestHandlerType(Class<? extends Request> requestType);

    public RequestHandler resolveRequestHandler(Class<? extends Request> requestType) {
        if(!requestHandlerTypeRegistry.containsKey(requestType)) {
            Class<? extends RequestHandler> requestHandlerType = resolveRequestHandlerType(requestType);
            registerRequestHandlerType(requestType, requestHandlerType);
        }
        RequestHandler handler = container.resolve(requestHandlerTypeRegistry.get(requestType));
        if(handler != null) {
            return handler;
        }
        throw new RequestHandlerResolutionException("Cannot resolve request handler for request type " + requestType.getName());
    }
}
