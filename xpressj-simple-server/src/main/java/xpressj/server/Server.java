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

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by akamensky on 8/4/14.
 */
class Server {

    private ServerConfiguration configuration;
    private ServerSocket serverSocket;
    private ThreadPoolExecutor pool;
    private BlockingQueue<Runnable> queue;
    private boolean isRunning;
    private boolean isStopping;
    private Server server;

    public Server(){
        this.server = this;
        this.queue = new ArrayBlockingQueue<>(5000);
        this.pool = new ThreadPoolExecutor(10, 500, new Long(1000), TimeUnit.MILLISECONDS, this.queue);
        this.pool.prestartAllCoreThreads();
        this.isRunning = false;
        this.isStopping = false;
    }

    public void setConfiguration(ServerConfiguration configuration){
        this.configuration = configuration;
    }

    public void start(){
        if (!this.isRunning && !this.isStopping) {
            this.isRunning = true;
            try {
                //TODO: use multiple ports!
                serverSocket = new ServerSocket(this.configuration.getPorts()[0]);
                while (this.isRunning) {
                    Socket clientSocket = serverSocket.accept();
                    this.pool.execute(new Runnable() {
                        @Override
                        public void run() {
                            try (
                                OutputStream out = clientSocket.getOutputStream();
                                InputStream in = clientSocket.getInputStream();
                            ) {
                                RequestImpl request = new RequestImpl(in);
                                ResponseImpl response = new ResponseImpl(out);

                                request.setResponse(response);
                                response.setRequest(request);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        if (!this.isStopping && this.isRunning) {
            try {
                this.isStopping = true;
                this.isRunning = false;
                while (this.pool.getActiveCount() != 0 || this.pool.getQueue().size() != 0) {
                    System.out.println(this.pool.getActiveCount());
                    Thread.sleep(1000);
                }
                this.serverSocket.close();
                this.pool.shutdown();
                this.pool.awaitTermination(60, TimeUnit.SECONDS);
                this.isStopping = false;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void restart() {
        this.stop();
        this.start();
    }

}
