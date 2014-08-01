package xpressj.route;

import java.util.List;

/**
 * Created by akamensky on 8/1/14.
 */
public interface RouteMatcher {
    public List<RouteImpl> getMatchingRoutes(String httpMethod, String uri);
}
