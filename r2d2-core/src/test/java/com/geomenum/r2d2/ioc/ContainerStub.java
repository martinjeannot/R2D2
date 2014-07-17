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

package com.geomenum.r2d2.ioc;

import com.geomenum.r2d2.servicelayer.*;

public class ContainerStub implements Container {

    @Override
    public <T> T resolve(Class<T> type) {
        if(RequestHandlerA.class.equals(type)) {
            return (T) new RequestHandlerA();
        } else if(RequestHandlerB.class.equals(type)) {
            return (T) new RequestHandlerB();
        } else if(RequestHandlerC1.class.equals(type)) {
            return (T) new RequestHandlerC1();
        } else if(RequestHandlerC2.class.equals(type)) {
            return (T) new RequestHandlerC2();
        } else if(RequestHandlerX.class.equals(type)) {
            return (T) new RequestHandlerX();
        }
        return null;
    }
}
