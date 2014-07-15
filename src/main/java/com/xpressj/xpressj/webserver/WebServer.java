package com.xpressj.xpressj.webserver;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.CharBuffer;
import java.util.concurrent.*;

/**
 * Created by akamensky on 7/15/14.
 */
public class WebServer {

    private Configuration configuration;
    private ServerSocket serverSocket;
    private ThreadPoolExecutor pool;
    private BlockingQueue<Runnable> queue;
    private boolean isRunning;
    private boolean isStopping;
    private WebServer server;

    public WebServer(Configuration configuration) {
        this.configuration = configuration;
        this.queue = new ArrayBlockingQueue<Runnable>(5000);
        this.pool = new ThreadPoolExecutor(10, 500, new Long(1000), TimeUnit.MILLISECONDS, this.queue);
        this.pool.prestartAllCoreThreads();
        this.isRunning = false;
        this.isStopping = false;
    }

    public void start() throws Exception {
        this.server = this;
        if (!this.isRunning && !this.isStopping) {
            this.isRunning = true;
            try {
                serverSocket = new ServerSocket(this.configuration.getPort());
                while (this.isRunning) {
                    Socket clientSocket = serverSocket.accept();
                    this.pool.execute(new Runnable() {
                        @Override
                        public void run() {
                            try (
                                OutputStream out = clientSocket.getOutputStream();
                                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                            ) {
                                //Read request
                                String inputLine, outputLine;
                                boolean requestHasBody;
                                StringBuilder builder = new StringBuilder();

                                while ((inputLine = in.readLine()) != null) {
                                    if (inputLine.contains("/test")){
                                        Thread.sleep(60000);
                                    } else if (inputLine.contains("/shutdown")) {
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    server.stop();
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }).start();
                                    } else if (inputLine.contains("/restart")) {
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    server.restart();
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }).start();
                                    }

                                    if (inputLine.length() == 0) {
//                                        System.out.println("Finished input");
                                        outputLine = "Bye.";
                                        out.write(outputLine.getBytes());
                                        out.flush();
//                                        System.out.println("Finished output");
                                        clientSocket.close();
                                        break;
                                    }
                                }
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

    public void stop() throws Exception {
        if (!this.isStopping && this.isRunning) {
            System.out.println("Waiting for all threads to finish");
            this.isStopping = true;
            this.isRunning = false;
            while (this.pool.getActiveCount() != 0 || this.pool.getQueue().size() != 0) {
                System.out.println(this.pool.getActiveCount());
                Thread.sleep(1000);
            }
            System.out.println("Closing socket connection");
            this.serverSocket.close();
            this.pool.shutdown();
            this.pool.awaitTermination(60, TimeUnit.SECONDS);
            this.isStopping = false;
            System.out.println("Stopped");
        }
    }

    public void restart() throws Exception {
        this.stop();
        this.start();
    }
}
