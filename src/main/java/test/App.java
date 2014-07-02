package test;

import xpressj.*;

/**
 * Created by akamensky on 7/2/14.
 */
public class App {

    public static void main(String[] args){
        XpressJ.start(new Configuration());

        XpressJ.get("*", new Route() {
            @Override
            public void handle(Request request, Response response) {
                response.send("Hello World!");
            }
        });
    }

}
