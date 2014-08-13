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

import xpressj.route.RouteImpl;
import xpressj.route.RouteMatcher;
import xpressj.server.Request;
import xpressj.server.Response;

import java.util.List;

/**
 * Created by akamensky on 7/30/14.
 */
public class RequestHandler implements xpressj.server.RequestHandler {
    private RouteMatcher routeMatcher;

    public RequestHandler(RouteMatcher routeMatcher) {
        this.routeMatcher = routeMatcher;
    }

    public boolean doHandle(Request req, Response res) throws Exception {
        boolean result = false;

        //Get routeMatcher
        List<RouteImpl> routes = routeMatcher.getMatchingRoutes(req.getHttpMethod(), req.getUri());
        for (RouteImpl route : routes) {

            route.handle(req, res);

            if (res.isConsumed()) {
                return true;
            }
        }

        return result;
    }
}
