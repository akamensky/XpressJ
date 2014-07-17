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

package xpressj.server;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.session.SessionHandler;
import xpressj.Response;
import xpressj.RouteImpl;
import xpressj.route.RouteMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by akamensky on 6/17/14.
 */
public class JettyHandler extends SessionHandler {

    private RouteMatcher routeMatcher;

    public JettyHandler(RouteMatcher routeMatcher){
        this.routeMatcher = routeMatcher;
    }

    @Override
    public void doHandle(String target, Request baseRequest, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException, ServletException {

        boolean isHandled = false;

        //Get proper request and response objects
        xpressj.Request req = new xpressj.Request(httpRequest);
        Response res = new Response(httpResponse);

        //Get routeMatcher
        List<RouteImpl> routes = routeMatcher.getMatchingRoutes(req.getHttpMethod(), req.getUri());
        for(RouteImpl route : routes){
            route.handle(req, res);
            if (res.isConsumed()){
                isHandled = true;
                break;
            }
        }

        baseRequest.setHandled(isHandled);

        //TODO: Add 404 & 500 processing

    }
}
