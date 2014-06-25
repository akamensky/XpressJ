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

package xpressj.route;

import xpressj.RouteImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akamensky on 6/17/14.
 */
public class RouteMatcher {

    private List<RouteImpl> routes;

    public RouteMatcher(){
        routes = new ArrayList<RouteImpl>();
    }

    public void addRoute(String httpMethod, RouteImpl route) {
        //TODO: Implement validation of route URI
        routes.add(route);
    }

    public RouteImpl getMatchingRoutes(String uri) {
        //TODO: Temporary stub instead of proper route matching
        return routes.get(0);
    }
}
