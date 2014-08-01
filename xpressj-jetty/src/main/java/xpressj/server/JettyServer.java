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

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.util.resource.Resource;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by akamensky on 7/30/14.
 */
public class JettyServer {

    private Server server;
    private SessionFactory sessionFactory;
    private RequestHandler handler;
    private ServerConfiguration configuration;

    public JettyServer(ServerConfiguration configuration) {
        this.configuration = configuration;
        this.handler = new RequestHandler(this.configuration.getRouteMatcher());
        org.eclipse.jetty.util.log.Log.setLog(new JettyLogger());
    }

    private static ServerConnector createSocketConnector() {
        return new ServerConnector(new Server());
    }

    public void start(Object lock) {
        if (this.configuration.useSessions()) {
            this.sessionFactory = new SessionFactory(this.configuration);
            this.handler.setSessionFactory(this.sessionFactory);
        }

        ServerConnector connector = createSocketConnector();

        connector.setIdleTimeout(TimeUnit.HOURS.toMillis(1));
        connector.setSoLingerTime(-1);
        connector.setHost(this.configuration.getHost());
        connector.setPort(this.configuration.getPort());

        this.server = connector.getServer();
        this.server.setConnectors(new Connector[]{connector});

        this.server.setStopTimeout(0);

        List<Handler> handlerList = new ArrayList<>();

        //Add main handler
        handlerList.add(this.handler);
        //Add bundled static files handler
        if (this.configuration.getStaticFilesLocation() != null) {
            ResourceHandler resourceHandler = new ResourceHandler();
            Resource staticResources = Resource.newClassPathResource(this.configuration.getStaticFilesLocation());
            resourceHandler.setBaseResource(staticResources);
            handlerList.add(resourceHandler);
        }
        //Add external static files handler
        if (this.configuration.getExternalStaticFilesLocation() != null) {
            ResourceHandler externalResourceHandler = new ResourceHandler();
            Resource externalStaticResources = Resource.newResource(new File(this.configuration.getExternalStaticFilesLocation()));
            externalResourceHandler.setBaseResource(externalStaticResources);
            handlerList.add(externalResourceHandler);
        }
        //Add error handler
        handlerList.add(new ErrorHandler(this.configuration));

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(handlerList.toArray(new Handler[handlerList.size()]));
        this.server.setHandler(handlers);

        try {
            this.server.start();

            System.out.println("** " + this.configuration.NAME + " has started ...");
            System.out.println("** Listening on " + this.configuration.getHost() + ":" + this.configuration.getPort());

            synchronized (lock) {//TODO: get rid of this somehow?
                lock.notifyAll();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        System.out.println("** " + this.configuration.NAME + " is stopping ...");
        try {
            if (server != null) {
                server.stop();
                if (this.sessionFactory != null) {
                    this.sessionFactory = null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("** " + this.configuration.NAME + " has stopped ...");
    }
}
