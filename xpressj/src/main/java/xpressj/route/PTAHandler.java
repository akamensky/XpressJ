package xpressj.route;

import com.augustl.pathtravelagent.IRouteHandler;
import com.augustl.pathtravelagent.RouteMatch;

import java.util.*;

public class PTAHandler implements IRouteHandler<PTARequest, PTAResponse> {
    private final Map<String, List<Route>> routes;

    public PTAHandler(String httpMethod, Route route) {
        Map<String, List<Route>> theRoutes = new HashMap<>();
        theRoutes.put(httpMethod, createInitialRoutes(route));
        this.routes = Collections.unmodifiableMap(theRoutes);
    }

    private PTAHandler(Map<String, List<Route>> routes) {
        this.routes = Collections.unmodifiableMap(routes);
    }

    @Override
    public IRouteHandler<PTARequest, PTAResponse> merge(IRouteHandler<PTARequest, PTAResponse> other) {
        if (other instanceof PTAHandler) {
            Map<String, List<Route>> theRoutes = new HashMap<>();
            theRoutes.putAll(this.routes);

            Map<String, List<Route>> otherRoutes = ((PTAHandler)other).routes;
            for (String method : otherRoutes.keySet()) {
                if (theRoutes.containsKey(method)) {
                    List<Route> xs = new ArrayList<>();
                    xs.addAll(theRoutes.get(method));
                    xs.addAll(otherRoutes.get(method));
                    theRoutes.put(method, xs);
                } else {
                    theRoutes.put(method, new ArrayList<>(otherRoutes.get(method)));
                }
            }

            return new PTAHandler(theRoutes);
        }

        return null;
    }

    @Override
    public PTAResponse call(RouteMatch<PTARequest> req) {
        String method = req.getRequest().getHttpMethod();
        if (this.routes.containsKey(method)) {
            return new PTAResponse(this.routes.get(method), req.getRouteMatchResult().getStringMatches());
        }

        if (this.routes.containsKey(null)) {
            return new PTAResponse(this.routes.get(null), req.getRouteMatchResult().getStringMatches());
        }

        return null;
    }

    private List<Route> createInitialRoutes(Route initialRoute) {
        List<Route> res = new ArrayList<>();
        res.add(initialRoute);
        return res;
    }
}
