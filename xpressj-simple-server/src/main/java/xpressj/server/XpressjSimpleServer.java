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
 * Created by akamensky on 8/2/14.
 */
public class XpressjSimpleServer implements Webserver {

    private Server server;
    private ServerConfiguration configuration;
    private boolean isConfigurationSet;

    public XpressjSimpleServer(){
        this.server = new Server();
    }

    public void setConfiguration(ServerConfiguration configuration) {
        this.configuration = configuration;
        this.server.setConfiguration(configuration);
        this.isConfigurationSet = true;
    }

    public void setHandler(RequestHandler handler) {

    }

    public void start() {
        if (isConfigurationSet) {
            this.server.start();
        } else
            throw new RuntimeException("Configuration must be set!");
    }

    public void stop() {
        this.server.stop();
    }

    public void restart() {
        this.server.restart();
    }
}
