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

package com.geomenum.r2d2.servicelayer.servicelayerconfiguration;

import com.geomenum.r2d2.common.RequestA;
import com.geomenum.r2d2.common.RequestB;
import com.geomenum.r2d2.common.RequestC1;
import com.geomenum.r2d2.common.RequestC2;
import com.geomenum.r2d2.servicelayer.*;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;

@Test
public class RequestHandlerRegistrationTests {

    private ServiceLayerConfiguration serviceLayerConfiguration = new ServiceLayerConfigurationStub();

    public void basicRequestHandlerRegistration() {
        ((ServiceLayerConfigurationStub) serviceLayerConfiguration).registerRequestHandlerType(RequestHandlerA.class);
        // our only way to test a successful registration is to ask for the resolution of the corresponding request
        assertNotNull(serviceLayerConfiguration.resolveRequestHandler(RequestA.class));
    }

    public void genericRequestHandlerRegistration() {
        ((ServiceLayerConfigurationStub) serviceLayerConfiguration).registerRequestHandlerType(RequestHandlerB.class);
        assertNotNull(serviceLayerConfiguration.resolveRequestHandler(RequestB.class));
    }

    public void advancedRequestHandlerRegistration1() {
        ((ServiceLayerConfigurationStub) serviceLayerConfiguration).registerRequestHandlerType(RequestHandlerC1.class);
        assertNotNull(serviceLayerConfiguration.resolveRequestHandler(RequestC1.class));
    }

    public void advancedRequestHandlerRegistration2() {
        ((ServiceLayerConfigurationStub) serviceLayerConfiguration).registerRequestHandlerType(RequestHandlerC2.class);
        assertNotNull(serviceLayerConfiguration.resolveRequestHandler(RequestC2.class));
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Found two request handlers that handle the same request type : com.geomenum.r2d2.common.RequestA\n"
                    + "First request handler  : com.geomenum.r2d2.servicelayer.RequestHandlerA\n"
                    + "Second request handler : com.geomenum.r2d2.servicelayer.RequestHandlerZ\n"
                    + "For each request type there must be only one request handler.")
    public void throwsExceptionWhenRegisteringAlreadyRegisteredRequestHandler() {
        ((ServiceLayerConfigurationStub) serviceLayerConfiguration).registerRequestHandlerType(RequestHandlerZ.class);
    }
}
