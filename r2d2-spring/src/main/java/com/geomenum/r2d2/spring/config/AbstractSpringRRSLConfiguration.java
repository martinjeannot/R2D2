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

package com.geomenum.r2d2.spring.config;

import com.geomenum.r2d2.spring.common.SpringRequestDispatcher;
import com.geomenum.r2d2.spring.common.SpringRequestDispatcherFactory;
import com.geomenum.r2d2.spring.ioc.SpringContainer;
import com.geomenum.r2d2.spring.servicelayer.SpringRequestProcessor;
import com.geomenum.r2d2.spring.servicelayer.SpringServiceLayerConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * Base class for Spring Request/Response Service Layer configuration using JavaConfig.
 */
@Configuration
public abstract class AbstractSpringRRSLConfiguration {

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public SpringContainer container() {
        return new SpringContainer(applicationContext);
    }

    @Bean
    public SpringServiceLayerConfiguration serviceLayerConfiguration() {
        return new SpringServiceLayerConfiguration(container());
    }

    @Bean
    public SpringRequestDispatcherFactory requestDispatcherFactory() {
        return new SpringRequestDispatcherFactory(container());
    }

    @Bean
    public SpringRequestProcessor requestProcessor() {
        return new SpringRequestProcessor(serviceLayerConfiguration());
    }

    @Bean
    @Scope("prototype")
    public SpringRequestDispatcher requestDispatcher() {
        return new SpringRequestDispatcher(requestProcessor());
    }
}
