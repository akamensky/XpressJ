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

import xpressj.route.Route;
import xpressj.route.RouteMatcher;

/**
 * Created by akamensky on 8/1/14.
 */
public interface ServerConfiguration {
    public static final String NAME = "XpressJ";

    public int getPort();

    public String getHost();

    public String getStaticFilesLocation();

    public String getExternalStaticFilesLocation();

    public Route getNotFoundPage();

    public Route getErrorPage();

    public int getSessionMaxAge();

    public String getSessionCookieName();

    public boolean useSessions();

    public RouteMatcher getRouteMatcher();
}
