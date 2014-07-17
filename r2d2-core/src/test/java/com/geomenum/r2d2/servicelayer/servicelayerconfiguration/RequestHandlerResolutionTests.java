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
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

@Test
public class RequestHandlerResolutionTests {

    private ServiceLayerConfiguration serviceLayerConfiguration = new ServiceLayerConfigurationStub();

    @BeforeClass
    public void setUp() {
        ((ServiceLayerConfigurationStub) serviceLayerConfiguration).registerRequestHandlerType(RequestHandlerA.class);
        ((ServiceLayerConfigurationStub) serviceLayerConfiguration).registerRequestHandlerType(RequestHandlerB.class);
        ((ServiceLayerConfigurationStub) serviceLayerConfiguration).registerRequestHandlerType(RequestHandlerC1.class);
        ((ServiceLayerConfigurationStub) serviceLayerConfiguration).registerRequestHandlerType(RequestHandlerC2.class);
    }

    public void basicRequestHandlerResolution() {
        RequestHandler resolvedHandler = serviceLayerConfiguration.resolveRequestHandler(RequestA.class);

        assertNotNull(resolvedHandler);
        assertEquals(resolvedHandler.getClass(), RequestHandlerA.class);
    }

    public void genericRequestHandlerResolution() {
        RequestHandler resolvedHandler = serviceLayerConfiguration.resolveRequestHandler(RequestB.class);

        assertNotNull(resolvedHandler);
        assertEquals(resolvedHandler.getClass(), RequestHandlerB.class);
    }

    public void advancedRequestHandlerResolution1() {
        RequestHandler resolvedHandler = serviceLayerConfiguration.resolveRequestHandler(RequestC1.class);

        assertNotNull(resolvedHandler);
        assertEquals(resolvedHandler.getClass(), RequestHandlerC1.class);
    }

    public void advancedRequestHandlerResolution2() {
        RequestHandler resolvedHandler = serviceLayerConfiguration.resolveRequestHandler(RequestC2.class);

        assertNotNull(resolvedHandler);
        assertEquals(resolvedHandler.getClass(), RequestHandlerC2.class);
    }

    @Test(expectedExceptions = RequestHandlerResolutionException.class,
            expectedExceptionsMessageRegExp = "Cannot resolve request handler for request type com.geomenum.r2d2.common.RequestA")
    public void throwsExceptionWhenRequestHandlerResolutionFailed() {
        ServiceLayerConfiguration serviceLayerConfiguration = new ServiceLayerConfigurationStub();
        ((ServiceLayerConfigurationStub) serviceLayerConfiguration).registerRequestHandlerType(RequestHandlerZ.class);

        serviceLayerConfiguration.resolveRequestHandler(RequestA.class);
    }
}
