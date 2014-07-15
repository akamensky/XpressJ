package test;

import com.xpressj.xpressj.webserver.Configuration;
import com.xpressj.xpressj.webserver.WebServer;

/**
 * Created by akamensky on 7/15/14.
 */
public class App {
    public static void main(String[] args){
        WebServer server = new WebServer(new Configuration());
        server.start();
    }
}
