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

import xpressj.Request;
import xpressj.Response;
import xpressj.RouteImpl;
import xpressj.route.RouteMatcher;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by akamensky on 6/17/14.
 */
public class RequestHandler implements Filter {

    private RouteMatcher routeMatcher;

    public RequestHandler(RouteMatcher routeMatcher){
        this.routeMatcher = routeMatcher;
    }

    public void init(FilterConfig filterConfig){}

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException{
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        //Get proper request and response objects
        Request req = new Request(httpRequest);
        Response res = new Response(httpResponse);

        //Get routeMatcher
        List<RouteImpl> routes = routeMatcher.getMatchingRoutes(req.getHttpMethod(), req.getUri());
        for(RouteImpl route : routes){
            route.handle(req, res);
            if (res.isConsumed()){
                break;
            }
        }

        //TODO: Add 404 & 500 processing
    }

    public void destroy(){}

}
