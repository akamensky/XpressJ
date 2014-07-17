package example;

import xpressj.*;

/**
 * Created by akamensky on 7/2/14.
 */
public class App {

    public static void main(String[] args){
        XpressJ app = new XpressJ(new Configuration().setStaticFilesLocation("/public"));
        app.start();

        //Match GET requests to "/"
        app.get("/", new Route() {
            @Override
            public void handle(Request request, Response response) {
                response.send("Hello");
            }
        });

        //Match ALL requests to "/all"
        app.all("/all", new Route() {
            @Override
            public void handle(Request request, Response response) {
                response.send("All requests!");
            }
        });

    }

}
