package xpressj.route;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PTAResponse {
    private final List<Route> routes;
    private final Map<String, String> routeParams;

    public PTAResponse(List<Route> routes, Map<String, String> routeParams) {
        this.routes = routes;
        this.routeParams = routeParams;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public Map<String, String> getRouteParams() {
        return routeParams;
    }
}
