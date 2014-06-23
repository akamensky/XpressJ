/*
 * Copyright 2014 - Alexey Kamenskiy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package xpressj.server;

import org.slf4j.Logger;
import xpressj.route.RouteMatcher;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by akamensky on 6/17/14.
 */
public class RequestHandler implements Filter {

    private RouteMatcher routeMatcher;

    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(RequestHandler.class);

    public RequestHandler(RouteMatcher routeMatcher){
        this.routeMatcher = routeMatcher;
    }

    public void init(FilterConfig filterConfig){}

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException{
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;


    }

    public void destroy(){}

    private static final String NOT_FOUND = "<html><body><h2>404 Not found</h2>The requested route [%s] has not been mapped in XpressJ</body></html>";
    private static final String INTERNAL_ERROR = "<html><body><h2>500 Internal Error</h2></body></html>";
}
