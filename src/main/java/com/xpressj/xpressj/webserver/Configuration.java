package com.xpressj.xpressj.webserver;

/**
 * Created by akamensky on 7/15/14.
 */
public class Configuration {
    private static final int DEFAULT_PORT = 8080;
    private static final String DEFAULT_HOST = "0.0.0.0";

    private int port = DEFAULT_PORT;
    private String host = DEFAULT_HOST;

    public Configuration setPort(int port){
        this.port = port;
        return this;
    }

    public Configuration setHost(String host){
        this.host = host;
        return this;
    }

    public int getPort(){
        return this.port;
    }

    public String getHost(){
        return this.host;
    }
}
