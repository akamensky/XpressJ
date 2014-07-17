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

import xpressj.route.HttpMethod;
import xpressj.route.RouteMatcher;
import xpressj.server.JettyHandler;
import xpressj.server.RequestHandler;
import xpressj.server.WebServer;

public final class XpressJ {

    private Configuration configuration;
    private boolean initialized = false;
    private WebServer server;
    private RouteMatcher routeMatcher;
    private Thread t;

    public XpressJ(final Configuration configuration){
        this.configuration = configuration;
        this.routeMatcher = new RouteMatcher(this.configuration);
    }

    public void start() {
        if(!initialized){
            final Object lock = new Object();

            t = new Thread(new Runnable() {
                @Override
                public void run() {
                    RequestHandler requestHandler = new RequestHandler(routeMatcher);
                    requestHandler.setConfiguration(configuration);
                    requestHandler.init(null);
                    JettyHandler handler = new JettyHandler(routeMatcher);
                    server = new WebServer(handler);
                    server.setConfiguration(configuration);
                    server.start(lock);
                }
            });
            t.start();

            synchronized (lock){
                try {
                    lock.wait();
                    initialized = true;
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
    }

    public void get(final String uri, final Route route){
        addRoute(HttpMethod.get.name(), new RouteImpl(HttpMethod.get.name(), uri, route));
    }

    public void post(final String uri, final Route route){
        addRoute(HttpMethod.post.name(), new RouteImpl(HttpMethod.post.name(), uri, route));
    }

    public void options(final String uri, final Route route){
        addRoute(HttpMethod.options.name(), new RouteImpl(HttpMethod.options.name(), uri, route));
    }

    public void head(final String uri, final Route route){
        addRoute(HttpMethod.head.name(), new RouteImpl(HttpMethod.head.name(), uri, route));
    }

    public void put(final String uri, final Route route){
        addRoute(HttpMethod.put.name(), new RouteImpl(HttpMethod.put.name(), uri, route));
    }

    public void delete(final String uri, final Route route){
        addRoute(HttpMethod.delete.name(), new RouteImpl(HttpMethod.delete.name(), uri, route));
    }

    public void trace(final String uri, final Route route){
        addRoute(HttpMethod.trace.name(), new RouteImpl(HttpMethod.trace.name(), uri, route));
    }

    public void all(final String uri, final Route route){
        addRoute(null, new RouteImpl(null, uri, route));
    }

    private void addRoute(String httpMethod, RouteImpl route){
        routeMatcher.addRoute(httpMethod, route);
    }

    public void stop() {
        server.stop();
    }

}
