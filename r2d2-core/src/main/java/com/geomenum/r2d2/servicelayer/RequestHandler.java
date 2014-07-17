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

public interface RequestHandler<REQ extends Request, RES extends Response> {

    RES handle(REQ request);

    RES createDefaultResponse();

    /**
     * This method is the non-generic version of the {@link #handle(com.geomenum.r2d2.common.Request)} method.
     * It is meant to be used by the {@link RequestProcessor} which has no idea of the actual type of the requests it
     * processes. Even though you can override this method for specific purposes, the real logic is called by the
     * parameterized version.
     *
     * @param request the {@link Request} to handle
     * @return the resulting {@link Response}
     * @see #handle(com.geomenum.r2d2.common.Request)
     */
    Response handleRequest(Request request);
}
