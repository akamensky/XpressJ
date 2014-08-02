package xpressj.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by akamensky on 8/2/14.
 */
public class XpressjSimpleServer implements Webserver {
    private ServerConfiguration configuration;
    private ServerSocket serverSocket;
    private ThreadPoolExecutor pool;
    private BlockingQueue<Runnable> queue;
    private boolean isRunning;
    private boolean isStopping;
    private XpressjSimpleServer server;

    public XpressjSimpleServer(){
        this.queue = new ArrayBlockingQueue<Runnable>(5000);
        this.pool = new ThreadPoolExecutor(10, 500, new Long(1000), TimeUnit.MILLISECONDS, this.queue);
        this.pool.prestartAllCoreThreads();
        this.isRunning = false;
        this.isStopping = false;
    }

    public void setConfiguration(ServerConfiguration configuration) {
        this.configuration = configuration;
    }

    public void start() {
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
                                    if (inputLine.contains("/test")) {
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

    public void stop() {
        if (!this.isStopping && this.isRunning) {
            try {
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
