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
import org.eclipse.jetty.server.handler.ResourceHandler;
import xpressj.Response;
import xpressj.RouteImpl;
import xpressj.SessionFactory;
import xpressj.route.RouteMatcher;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by akamensky on 6/17/14.
 */
public class RequestHandler extends ResourceHandler {

    private RouteMatcher routeMatcher;
    private static final MultipartConfigElement MULTI_PART_CONFIG = new MultipartConfigElement(System.getProperty("java.io.tmpdir"));
    private SessionFactory sessionFactory;

    public RequestHandler(RouteMatcher routeMatcher){
        this.routeMatcher = routeMatcher;
    }

    public void setSessionFactory(SessionFactory factory){
        this.sessionFactory = factory;
    }

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException, ServletException {

        //Hack to make file upload work
        boolean isMultipart = false;
        if (httpRequest.getContentType() != null && httpRequest.getContentType().startsWith("multipart/form-data")) {
            baseRequest.setAttribute(Request.__MULTIPART_CONFIG_ELEMENT, MULTI_PART_CONFIG);
            isMultipart = true;
        }

        //Get proper request and response objects
        Response res = new Response(httpResponse);
        xpressj.Request req = new xpressj.Request(httpRequest, res, isMultipart, this.sessionFactory);
        //TODO: this is ugly, need to rethink this one

        //set delegates so that req and res can communicate with each other
        req.setDelegate(res);
        res.setDelegate(req);

        //Get routeMatcher
        List<RouteImpl> routes = routeMatcher.getMatchingRoutes(req.getHttpMethod(), req.getUri());
        for(RouteImpl route : routes){
            try {
                route.handle(req, res);
            } catch (Exception e) {
                httpResponse.setStatus(500);
                e.printStackTrace();
            }
            if (res.isConsumed()){
                baseRequest.setHandled(true);
                break;
            }
        }
    }
}
