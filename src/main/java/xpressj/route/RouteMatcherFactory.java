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

import org.slf4j.Logger;

/**
 * Created by akamensky on 6/17/14.
 */
public class RouteMatcherFactory {
    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(RouteMatcherFactory.class);

    private static RouteMatcher routeMatcher = null;

    public static synchronized RouteMatcher get(){
        if(routeMatcher == null){
            LOG.debug("New RouteMatcher");
            routeMatcher = new RouteMatcher();
        }
        return routeMatcher;
    }
}
