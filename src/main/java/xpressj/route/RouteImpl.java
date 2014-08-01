package xpressj.route;

import xpressj.server.Request;
import xpressj.server.Response;

/**
 * Created by akamensky on 8/1/14.
 */
public interface RouteImpl {
    public boolean match(String httpMethod, String path);

    public void handle(Request req, Response res) throws Exception;
}
