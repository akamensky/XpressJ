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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import xpressj.route.*;
import xpressj.server.Request;
import xpressj.server.Response;

import java.util.List;

/**
 * Created by akamensky on 8/13/14.
 */
public class RouteMatchingTest {
    RouteMatcherImpl routeMatcher;
    String[] routes = new String[]{
            "*",
            "/",
            "/test",
            "/test/*",
            "/test/*/test",
            "/test/:param/test",
    };

    @Before
    public void setup() {
        Configuration configuration = new Configuration();
        this.routeMatcher = new RouteMatcherImpl(configuration);

        for (String route : this.routes) {
            this.routeMatcher.addRoute(null, new RouteImplImpl(null, route, new Route() {
                @Override
                public void handle(Request request, Response response) throws Exception {
                    response.send("test");
                }
            }));
        }
    }

    @Test
    public void number_of_routes_test() {
        List<RouteImpl> results;

        results = this.routeMatcher.getMatchingRoutes("GET", "/");
        Assert.assertEquals(results.size(), 2);

        results = this.routeMatcher.getMatchingRoutes("GET", "/test");
        Assert.assertEquals(results.size(), 2);

        results = this.routeMatcher.getMatchingRoutes("GET", "/test/whatever/whatever/whatever");
        Assert.assertEquals(results.size(), 2);

        results = this.routeMatcher.getMatchingRoutes("GET", "/test/whatever/test");
        Assert.assertEquals(results.size(), 4);
    }

    @Test
    public void order_of_routes_test() {
        List<RouteImpl> results;

        results = this.routeMatcher.getMatchingRoutes("GET", "/");
        Assert.assertEquals(results.get(0).getPath(), "*");
        Assert.assertEquals(results.get(1).getPath(), "/");

        results = this.routeMatcher.getMatchingRoutes("GET", "/test");
        Assert.assertEquals(results.get(0).getPath(), "*");
        Assert.assertEquals(results.get(1).getPath(), "/test");

        results = this.routeMatcher.getMatchingRoutes("GET", "/test/whatever/whatever/whatever");
        Assert.assertEquals(results.get(0).getPath(), "*");
        Assert.assertEquals(results.get(1).getPath(), "/test/*");

        results = this.routeMatcher.getMatchingRoutes("GET", "/test/whatever/test");
        Assert.assertEquals(results.get(0).getPath(), "*");
        Assert.assertEquals(results.get(1).getPath(), "/test/*");
        Assert.assertEquals(results.get(2).getPath(), "/test/*/test");
        Assert.assertEquals(results.get(3).getPath(), "/test/:param/test");
    }
}
