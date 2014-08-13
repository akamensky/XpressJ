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

/**
 * Created by akamensky on 7/29/14.
 */
public interface Webserver {

    /**
     * Set configuration that is needed for server
     *
     * @param configuration
     */
    public void setConfiguration(ServerConfiguration configuration);

    /**
     * Suppose to start web server using configuration
     */
    public abstract void start();

    /**
     * Suppose to stop web server (app should exit if no other server running)
     */
    public abstract void stop();

    /**
     * Suppose to restart web server (without exiting app)
     */
    public abstract void restart();

    /**
     * Sets handler implementation
     */
    public void setHandler(RequestHandler handler);
}
