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

import xpressj.template.Template;

/**
 * Created by akamensky on 7/29/14.
 */
public final class XpressjJetty implements Webserver {

    private ServerConfiguration configuration;
    private RequestHandler handler;
    private boolean initialized = false;
    private JettyServer server;
    private Thread t;
    private Template templateEngine;

    public void setConfiguration(ServerConfiguration configuration) {
        this.configuration = configuration;
    }

    public void setHandler(RequestHandler handler) {
        if (handler == null) {
            throw new RuntimeException("No handler present");
        }
        this.handler = handler;
    }

    public void start() {
        if (!initialized) {
            final Object lock = new Object();

            t = new Thread(new Runnable() {
                @Override
                public void run() {
                    server = new JettyServer(configuration);
                    server.setHandler(handler);
                    server.setTemplateEngine(templateEngine);
                    server.start(lock);
                }
            });
            t.start();

            synchronized (lock) {
                try {
                    lock.wait();
                    initialized = true;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void stop() {
        server.stop();
    }

    public void restart() {
        //Do nothing here yet
    }

    public void setTemplateEngine(Template templateEngine) {
        this.templateEngine = templateEngine;
    }
}
