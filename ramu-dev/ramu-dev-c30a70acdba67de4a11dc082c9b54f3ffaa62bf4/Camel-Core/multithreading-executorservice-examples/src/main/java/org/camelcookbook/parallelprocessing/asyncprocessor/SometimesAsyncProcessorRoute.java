/*
 * Copyright (C) Scott Cranton, Jakub Korab, and Christian Posta
 * https://github.com/CamelCookbook
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.camelcookbook.parallelprocessing.asyncprocessor;

import org.apache.camel.builder.RouteBuilder;

/**
 * Demonstrates the use of an {@link org.apache.camel.AsyncProcessor} that can also respond synchronously.
 */
public class SometimesAsyncProcessorRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("seda:in?concurrentConsumers=5")
            .to("direct:in")
            .log("Processed by:${threadName}");

        from("direct:in")
            .log("Processing ${body}:${threadName}")
            .setHeader("initiatingThread", simple("${threadName}"))
            .process(new HeaderDrivenSlowOperationProcessor())
            .setHeader("completingThread", simple("${threadName}"))
            .log("Completed ${body}:${threadName}")
            .to("mock:out");

    }
}
