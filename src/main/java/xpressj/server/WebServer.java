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

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import xpressj.JettyLogger;

public class WebServer {

    private static final String NAME = "XPRESSJ";
    private Handler handler;
    private Server server;

    public WebServer(Handler handler){
        this.handler = handler;
        org.eclipse.jetty.util.log.Log.setLog(new JettyLogger());
    }

    public void start(String host, int port, Object lock){

        ServerConnector connector = createSocketConnector();

        connector.setIdleTimeout(TimeUnit.HOURS.toMillis(1));
        connector.setSoLingerTime(-1);
        connector.setHost(host);
        connector.setPort(port);

        server = connector.getServer();
        server.setConnectors(new Connector[] {connector});

        server.setHandler(handler);

        try{
            server.start();

            System.out.println("** " + NAME + " has started ...");
            System.out.println("** Listening on " + host + ":" + port);

            synchronized (lock){
                lock.notifyAll();
            }
        } catch (Exception e){
            e.printStackTrace();
            System.exit(100);
        }
    }

    public void stop(){
        System.out.println("** " + NAME + " is stopping ...");
        try{
            if(server != null){
                server.stop();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(100);
        }
        System.out.println("** " + NAME + " has stopped ...");
    }

    private static ServerConnector createSocketConnector(){
        return new ServerConnector(new Server());
    }
}
