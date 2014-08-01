package example;

/**
 * Created by akamensky on 7/2/14.
 */
public class App {

    public static void main(String[] args) {
        XpressJ app = new XpressJ(new Configuration().setStaticFilesLocation("/public").enableSessions().setSessionMaxAge(10000));
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

        app.post("/fileupload", new Route() {
            @Override
            public void handle(Request request, Response response) {
                response.send("something");
            }
        });

        app.get("/error", new Route() {
            @Override
            public void handle(Request request, Response response) throws Exception {
                throw new RuntimeException("Custom error");
            }
        });

    }

}
