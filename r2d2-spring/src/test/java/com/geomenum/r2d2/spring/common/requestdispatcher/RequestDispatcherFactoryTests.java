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

package com.geomenum.r2d2.spring.common.requestdispatcher;

import com.geomenum.r2d2.common.RequestDispatcher;
import com.geomenum.r2d2.common.RequestDispatcherFactory;
import com.geomenum.r2d2.spring.config.RRSLTestConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNotNull;

@ContextConfiguration(classes = {RRSLTestConfiguration.class})
@Test
public class RequestDispatcherFactoryTests extends AbstractTestNGSpringContextTests {

    @Autowired
    private RequestDispatcherFactory requestDispatcherFactory;

    public void createRequestDispatcher() {
        RequestDispatcher requestDispatcher = requestDispatcherFactory.createRequestDispatcher();

        assertNotNull(requestDispatcher);
    }

    public void create2DifferentRequestDispatchers() {
        RequestDispatcher requestDispatcher1 = requestDispatcherFactory.createRequestDispatcher();
        RequestDispatcher requestDispatcher2 = requestDispatcherFactory.createRequestDispatcher();

        assertNotEquals(requestDispatcher1, requestDispatcher2);
        assertNotEquals(System.identityHashCode(requestDispatcher1), System.identityHashCode(requestDispatcher2));
    }
}
