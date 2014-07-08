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
import org.junit.Test;

/**
 * Created by akamensky on 7/3/14.
 */
public class RouteImplTest {

    @Test
    public void simple_route_matching1() {
        RouteImpl route = new RouteImpl("get", "/", null);
        Assert.assertEquals(true, route.match("get", "/"));
        Assert.assertNotEquals(true, route.match("get", "/test"));
    }

    @Test
    public void simple_route_matching2() {
        RouteImpl route = new RouteImpl("get", "/test", null);
        Assert.assertEquals(true, route.match("get", "/test"));
        Assert.assertNotEquals(true, route.match("get", "/"));
    }

    @Test
    public void wildcard_route_matching1() {
        RouteImpl route = new RouteImpl("get", "*", null);
        Assert.assertEquals(true, route.match("get", "/"));
        Assert.assertEquals(true, route.match("get", "/test"));
        Assert.assertEquals(true, route.match("get", "/test/test"));
    }

    @Test
    public void wildcard_route_matching2() {
        RouteImpl route = new RouteImpl("get", "/test/*", null);
        Assert.assertEquals(false, route.match("get", "/"));
        Assert.assertEquals(false, route.match("get", "/test"));
        Assert.assertEquals(true, route.match("get", "/test/test"));
    }

    @Test
    public void wildcard_route_matching3() {
        RouteImpl route = new RouteImpl("get", "/hello/*/world", null);
        Assert.assertEquals(false, route.match("get", "/hello/something"));
        Assert.assertEquals(false, route.match("get", "/hello/something/worlds"));
        Assert.assertEquals(false, route.match("get", "/hello/something/world/else"));
        Assert.assertEquals(true, route.match("get", "/hello/something/world"));
    }

    @Test
    public void wildcard_route_matching4() {
        RouteImpl route = new RouteImpl("get", "/test*/world*", null);
        Assert.assertEquals(true, route.match("get", "/testwords-/world/true"));
        Assert.assertEquals(true, route.match("get", "/test-phrase/worlds"));
        Assert.assertEquals(false, route.match("get", "/test-phrase/hello/worlds"));
    }

    @Test
    public void param_route_matching1() {
        RouteImpl route = new RouteImpl("get", "/test/:param", null);
        Assert.assertEquals(true, route.match("get", "/test/url"));
        Assert.assertEquals(false, route.match("get", "/test/url/wrong"));
        Assert.assertEquals(false, route.match("get", "/test"));
        Assert.assertEquals(false, route.match("get", "/"));
    }
    @Test
    public void param_route_matching2() {
        RouteImpl route = new RouteImpl("get", "/test-:param/:param", null);
        Assert.assertEquals(true, route.match("get", "/test-some/url"));
        Assert.assertEquals(false, route.match("get", "/test-some/url/wrong"));
        Assert.assertEquals(false, route.match("get", "/test/url"));
        Assert.assertEquals(false, route.match("get", "/"));
    }
    @Test
    public void param_route_matching3() {
        RouteImpl route = new RouteImpl("get", "/test/:param/hello", null);
        Assert.assertEquals(true, route.match("get", "/test/some/hello"));
        Assert.assertEquals(false, route.match("get", "/test/some/wrong"));
        Assert.assertEquals(false, route.match("get", "/test/wrong"));
        Assert.assertEquals(false, route.match("get", "/test/some/url/wrong"));
        Assert.assertEquals(false, route.match("get", "/"));
    }

    @Test
    public void param_extraction1(){
        RouteImpl route = new RouteImpl("get", "/test/:key/hello", new Route() {
            @Override
            public void handle(Request request, Response response) {
                Assert.assertEquals("value", request.getParam("key"));
            }
        });

        Request req = new Request("get", "/test/value/hello");
        Response res = new Response();
        route.handle(req, res);
    }

    @Test
    public void param_extraction2(){
        RouteImpl route = new RouteImpl("get", "/test/:key/hello/:key2", new Route() {
            @Override
            public void handle(Request request, Response response) {
                Assert.assertEquals("value", request.getParam("key"));
                Assert.assertEquals("value2", request.getParam("key2"));
            }
        });

        Request req = new Request("get", "/test/value/hello/value2");
        Response res = new Response();
        route.handle(req, res);
    }

    @Test
    public void param_extraction3(){
        RouteImpl route = new RouteImpl("get", "/test/*/:key/hello", new Route() {
            @Override
            public void handle(Request request, Response response) {
                Assert.assertEquals("value", request.getParam("key"));
            }
        });

        Request req = new Request("get", "/test/wrong/value/hello");
        Response res = new Response();
        route.handle(req, res);
    }

    @Test
    public void param_extraction4(){
        RouteImpl route = new RouteImpl("get", "/test/*/:key/hello", new Route() {
            @Override
            public void handle(Request request, Response response) {
                Assert.assertEquals("value", request.getParam("key"));
            }
        });

        Request req = new Request("get", "/test/wrong/value/hello");
        Response res = new Response();
        route.handle(req, res);

        route = new RouteImpl("get", "/test/*/value/hello", new Route() {
            @Override
            public void handle(Request request, Response response) {
                //Here we should not have any parameters
                Assert.assertNotEquals("value", request.getParam("key"));
                Assert.assertEquals(0, request.getParamsCount());
            }
        });

        route.handle(req, res);
    }
}
