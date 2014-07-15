package com.xpressj.xpressj.webserver;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.CharBuffer;

/**
 * Created by akamensky on 7/15/14.
 */
public class WebServer {

    private Configuration configuration;
    private ServerSocket serverSocket;

    public WebServer(Configuration configuration) {
        this.configuration = configuration;
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(this.configuration.getPort());
            while(true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new Runnable() {
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
                                System.out.println(inputLine);

                                if (inputLine.contains("Content-Length"))

                                if (inputLine.length() == 0){
                                    System.out.println("Finished input");
                                    outputLine = "Bye.";
                                    out.write(outputLine.getBytes());
                                    out.flush();
                                    System.out.println("Finished output");
                                    clientSocket.close();
                                    break;
                                }
                            }
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
