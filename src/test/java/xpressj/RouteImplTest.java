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

}
