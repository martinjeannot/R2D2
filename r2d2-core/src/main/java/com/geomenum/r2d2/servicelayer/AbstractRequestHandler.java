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
import com.geomenum.r2d2.common.Response;

public abstract class AbstractRequestHandler<REQ extends Request, RES extends Response> implements RequestHandler<REQ, RES> {

    @Override
    public abstract RES handle(REQ request);

    @Override
    public abstract RES createDefaultResponse();

    @Override
    public Response handleRequest(Request request) {
        REQ typedRequest = (REQ) request;
        beforeHandle(typedRequest);
        RES response = handle(typedRequest);
        afterHandle(typedRequest);
        return response;
    }

    protected void beforeHandle(REQ request) {}
    protected void afterHandle(REQ request) {}
}
