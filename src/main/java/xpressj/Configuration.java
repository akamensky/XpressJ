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

import xpressj.route.Route;
import xpressj.route.RouteMatcherImpl;
import xpressj.server.Request;
import xpressj.server.Response;
import xpressj.server.ServerConfiguration;
import xpressj.server.Webserver;

/**
 * Created by akamensky on 6/30/14.
 */
public class Configuration implements ServerConfiguration {
    public static final String NAME = "XpressJ";
    private static final int DEFAULT_PORT = 8080;
    private int port = DEFAULT_PORT;
    private static final String DEFAULT_HOST = "0.0.0.0";
    private String host = DEFAULT_HOST;
    private static final Route DEFAULT_NOT_FOUND_PAGE = new Route() {
        @Override
        public void handle(Request request, Response response) {
            response.send(404, "Not Found");
        }
    };
    private Route notFoundPage = DEFAULT_NOT_FOUND_PAGE;
    private static final Route DEFAULT_ERROR_PAGE = new Route() {
        @Override
        public void handle(Request request, Response response) throws Exception {
            response.send(500, "Internal Server Error");
        }
    };
    private Route errorPage = DEFAULT_ERROR_PAGE;
    private RouteMatcherImpl routeMatcher;
    private boolean cacheEnabled = false;
    private String staticFilesLocation;
    private String externalStaticFilesLocation;
    private boolean useSessions = false;
    private int sessionMaxAge = 0;
    private String sessionCookieName = "XPRESSJ_SESS";

    private Class webserverClass;

    public Configuration(Class webserverClass) {
        if (webserverClass == null || !Webserver.class.isAssignableFrom(webserverClass)) {
            throw new RuntimeException("Webserver class needed");
        } else {
            this.webserverClass = webserverClass;
        }
    }

    public int getPort() {
        return this.port;
    }

    public Configuration setPort(int port) {
        this.port = port;
        return this;
    }

    public String getHost() {
        return this.host;
    }

    public Configuration setHost(String host) {
        this.host = host;
        return this;
    }

    public Configuration setRouteCache(boolean flag) {
        this.cacheEnabled = flag;
        return this;
    }

    public boolean isCacheEnabled() {
        return this.cacheEnabled;
    }

    public String getStaticFilesLocation() {
        return this.staticFilesLocation;
    }

    public Configuration setStaticFilesLocation(String location) {
        this.staticFilesLocation = location;
        return this;
    }

    public String getExternalStaticFilesLocation() {
        return this.externalStaticFilesLocation;
    }

    public Configuration setExternalStaticFilesLocation(String location) {
        this.externalStaticFilesLocation = location;
        return this;
    }

    public Route getNotFoundPage() {
        return this.notFoundPage;
    }

    public Configuration setNotFoundPage(Route route) {
        if (route != null)
            this.notFoundPage = route;
        return this;
    }

    public Route getErrorPage() {
        return this.errorPage;
    }

    public Configuration setErrorPage(Route route) {
        if (route != null)
            this.errorPage = route;
        return this;
    }

    public Configuration enableSessions(String name, int maxAge) {
        this.useSessions = true;
        this.sessionCookieName = name;
        this.sessionMaxAge = maxAge;
        return this;
    }

    public int getSessionMaxAge() {
        return this.sessionMaxAge;
    }

    public String getSessionCookieName() {
        return this.sessionCookieName;
    }

    public Configuration enableSessions() {
        this.useSessions = true;
        return this;
    }

    public boolean useSessions() {
        return this.useSessions;
    }

    public RouteMatcherImpl getRouteMatcher() {
        return this.routeMatcher;
    }

    public void setRouteMatcher(final RouteMatcherImpl routeMatcher) {
        this.routeMatcher = routeMatcher;
    }

    public Class getWebserverClass() {
        return this.webserverClass;
    }
}
