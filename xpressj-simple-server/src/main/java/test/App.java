package test;

import xpressj.server.Webserver;
import xpressj.server.XpressjSimpleServer;

/**
 * Created by akamensky on 8/4/14.
 */
public class App {
    public static void main(String[] args){
        Webserver server = new XpressjSimpleServer();
        server.setConfiguration(new Configuration());

        server.start();
    }
}
