package xpressj.route;

import com.augustl.pathtravelagent.DefaultPathToPathSegments;
import com.augustl.pathtravelagent.DefaultRouteMatcher;
import com.augustl.pathtravelagent.RouteTreeNode;
import com.augustl.pathtravelagent.SingleRouteBuilder;
import xpressj.server.Request;
import xpressj.server.RequestHandler;
import xpressj.server.Response;

import java.util.List;
import java.util.Map;

public class PTARequestHandler implements RequestHandler {
    private RouteTreeNode<PTARequest, PTAResponse> r = new RouteTreeNode<>();
    private DefaultRouteMatcher<PTARequest, PTAResponse> matcher = new DefaultRouteMatcher<>();

    public void addRoute(String httpMethod, String uri, Route route) {
        List<String> pathSegments = DefaultPathToPathSegments.parse(uri);


        SingleRouteBuilder<PTARequest, PTAResponse> routeBuilder = new SingleRouteBuilder<>();

        for (String pathSegment : pathSegments) {
            if (pathSegment.equals("*")) {
                routeBuilder.param("*");
                continue;
            }

            if (pathSegment.startsWith(":")) {
                routeBuilder.param(pathSegment.substring(1));
                continue;
            }

            routeBuilder.path(pathSegment);
        }

        r = r.merge(routeBuilder.build(new PTAHandler(httpMethod, route)));
    }

    @Override
    public boolean doHandle(Request request, Response response) throws Exception {
        PTAResponse routeResponse = matcher.match(
            r,
            new PTARequest(
                request.getHttpMethod(),
                DefaultPathToPathSegments.parse(request.getUri())));

        if (routeResponse != null) {
            List<Route> routes = routeResponse.getRoutes();
            for (Map.Entry<String, String> entry : routeResponse.getRouteParams().entrySet()) {
                request.addParam(entry.getKey(), entry.getValue());
            }
            for (Route route : routes) {
                route.handle(request, response);
                if (response.isConsumed()) {
                    request.clearParams();
                    return true;
                }
            }
            request.clearParams();
        }

        return false;
    }
}
