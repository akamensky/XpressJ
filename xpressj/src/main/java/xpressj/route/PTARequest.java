package xpressj.route;

import com.augustl.pathtravelagent.IRequest;

import java.util.Arrays;
import java.util.List;

public class PTARequest implements IRequest {
    private final String httpMethod;
    private final List<String> pathSegments;

    public PTARequest(String httpMethod, List<String> pathSegments) {
        this.httpMethod = httpMethod;
        this.pathSegments = pathSegments;
    }

    @Override
    public List<String> getPathSegments() {
        return this.pathSegments;
    }

    public String getHttpMethod() {
        return this.httpMethod;
    }
}
