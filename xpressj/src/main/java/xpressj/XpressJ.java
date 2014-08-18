/*
 * Copyright 2014 - Alexey Kamenskiy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package xpressj;

import xpressj.route.*;
import xpressj.server.Webserver;
import xpressj.template.Template;

import java.util.ServiceLoader;

public final class XpressJ {

    private Configuration configuration;
    private boolean initialized = false;
    private Webserver server;
    private final PTARequestHandler requestHandler = new PTARequestHandler();
    private Template templateEngine;

    public XpressJ(final Configuration configuration) {
        this.configuration = configuration;

        //Initialise template engine
        ServiceLoader<Template> loader = ServiceLoader.load(Template.class);
        for (Template templateEngine : loader) {
            this.templateEngine = templateEngine;
            break;
        }
        this.templateEngine.initialize(this.configuration);
    }

    public void start() {
        try {
            this.server = (Webserver) this.configuration.getWebserverClass().newInstance();
            this.server.setConfiguration(this.configuration);
            this.server.setHandler(this.requestHandler);
            this.server.setTemplateEngine(this.templateEngine);
            this.server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void get(final String uri, final Route route) {
        addRoute(HttpMethod.get.name(), uri, route);
    }

    public void post(final String uri, final Route route) {
        addRoute(HttpMethod.post.name(), uri, route);
    }

    public void options(final String uri, final Route route) {
        addRoute(HttpMethod.options.name(),uri, route);
    }

    public void head(final String uri, final Route route) {
        addRoute(HttpMethod.head.name(), uri, route);
    }

    public void put(final String uri, final Route route) {
        addRoute(HttpMethod.put.name(), uri, route);
    }

    public void delete(final String uri, final Route route) {
        addRoute(HttpMethod.delete.name(), uri, route);
    }

    public void trace(final String uri, final Route route) {
        addRoute(HttpMethod.trace.name(), uri, route);
    }

    public void all(final String uri, final Route route) {
        addRoute(null, uri, route);
    }

    private void addRoute(String httpMethod, String uri, Route route) {
        this.requestHandler.addRoute(httpMethod, uri, route);
    }

    public void stop() {
        this.server.stop();
    }

}
