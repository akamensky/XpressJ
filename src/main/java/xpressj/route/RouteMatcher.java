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
import java.util.HashMap;
import java.util.List;

/**
 * Created by akamensky on 6/17/14.
 */
public class RouteMatcher {

    private List<RouteImpl> routes;
    private HashMap<String, List<RouteImpl>> routesCached;
    private static boolean isCacheEnabled = false;

    public RouteMatcher(){
        routes = new ArrayList<>();
        routesCached = new HashMap<>();
    }

    public void addRoute(String httpMethod, RouteImpl route) {
        //TODO: Implement validation of route URI
        routes.add(route);
    }

    public static void enableCache(boolean flag){
        isCacheEnabled = flag;
    }

    public List<RouteImpl> getMatchingRoutes(String httpMethod, String uri) {
        //TODO: Implement more proper (faster?) route matching

        //Check if this URI was already cached
        if (isCacheEnabled && routesCached.containsKey(httpMethod+":"+uri)){
            return routesCached.get(httpMethod+":"+uri);
        }

        List<RouteImpl> result = new ArrayList<>();
        for (RouteImpl route : routes){
            if (route.match(httpMethod, uri)){
                result.add(route);
            }
        }

        if (isCacheEnabled) {
            //Insert results to cache
            routesCached.put(httpMethod + ":" + uri, result);
        }

        return result;
    }
}
