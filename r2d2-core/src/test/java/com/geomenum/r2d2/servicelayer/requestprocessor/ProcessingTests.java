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

package com.geomenum.r2d2.servicelayer.requestprocessor;

import com.geomenum.r2d2.common.*;
import com.geomenum.r2d2.servicelayer.RequestHandlerA;
import com.geomenum.r2d2.servicelayer.RequestHandlerX;
import com.geomenum.r2d2.servicelayer.RequestProcessor;
import com.geomenum.r2d2.servicelayer.ServiceLayerConfiguration;
import com.geomenum.r2d2.servicelayer.servicelayerconfiguration.ServiceLayerConfigurationStub;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

@Test
public class ProcessingTests {

    private RequestProcessor requestProcessor;

    @BeforeClass
    public void setUp() {
        ServiceLayerConfiguration serviceLayerConfiguration = new ServiceLayerConfigurationStub();
        ((ServiceLayerConfigurationStub) serviceLayerConfiguration).registerRequestHandlerType(RequestHandlerA.class);
        ((ServiceLayerConfigurationStub) serviceLayerConfiguration).registerRequestHandlerType(RequestHandlerX.class);
        //((ServiceLayerConfigurationStub) serviceLayerConfiguration).registerRequestHandlerType(RequestHandlerB.class);
        //((ServiceLayerConfigurationStub) serviceLayerConfiguration).registerRequestHandlerType(RequestHandlerC1.class);
        //((ServiceLayerConfigurationStub) serviceLayerConfiguration).registerRequestHandlerType(RequestHandlerC2.class);

        requestProcessor = new RequestProcessorStub(serviceLayerConfiguration);
    }

    public void processSingleRequest() {
        Response[] responses = requestProcessor.process(new RequestA());

        assertNotNull(responses);
        assertEquals(responses.length, 1);
        assertEquals(responses[0].getClass(), ResponseA.class);
        assertEquals(responses[0].getException(), null);
        assertEquals(responses[0].getExceptionType(), ExceptionType.NONE);
    }

    public void processWithPreviouslyFailedRequest() {
        Response[] responses = requestProcessor.process(new RequestX(), new RequestA());

        assertNotNull(responses);
        assertEquals(responses.length, 2);
        assertEquals(responses[0].getClass(), ResponseX.class);
        assertEquals(responses[0].getException().getClass(), NullPointerException.class);
        assertEquals(responses[0].getExceptionType(), ExceptionType.UNKNOWN);
        assertEquals(responses[1].getClass(), ResponseA.class);
        assertEquals(responses[1].getException().getClass(), Exception.class);
        assertEquals(responses[1].getExceptionType(), ExceptionType.EARLIER_REQUEST_ALREADY_FAILED);
    }
}
