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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xpressj.route.HttpMethod;
import xpressj.route.RouteMatcher;
import xpressj.route.RouteMatcherFactory;
import xpressj.server.WebServer;
import xpressj.server.WebServerFactory;

public final class XpressJ {
    private static final Logger LOG = LoggerFactory.getLogger("xpressj.XpressJ");
    public static final int DEFAULT_PORT = 8080;

    protected static boolean initialized = false;

    protected static int port = DEFAULT_PORT;
    protected static String host = "0.0.0.0";

    protected static WebServer server;
    protected static RouteMatcher routeMatcher;

    public static synchronized void start(){
        if(!initialized){
            routeMatcher = RouteMatcherFactory.get();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    server = WebServerFactory.create();
                    server.start(host, port);
                }
            }).start();
            initialized = true;
        }
    }

    public static synchronized void get(final String uri, final Route route){
        addRoute(HttpMethod.get.name(), new RouteImpl(uri, route));
    }

    private static void addRoute(String httpMethod, RouteImpl route){
        routeMatcher.addRoute(httpMethod, route);
    }
}
