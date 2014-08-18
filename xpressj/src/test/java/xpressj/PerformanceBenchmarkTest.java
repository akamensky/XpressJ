/*
 * Copyright 2014 - Alexey Kamenskiy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package xpressj;

import com.carrotsearch.junitbenchmarks.AbstractBenchmark;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import xpressj.route.Route;
import xpressj.route.RouteImpl;
import xpressj.route.RouteMatcher;
import xpressj.server.Request;
import xpressj.server.Response;
import xpressj.util.TestUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by akamensky on 7/21/14.
 */
public class PerformanceBenchmarkTest extends AbstractBenchmark {
    private static final String[] routePaths = {
            "/",
            "/projects",
            "/projects/:projectId",
            "/projects/:projectId/todos",
            "/projects/:projectId/todos/:todoId",
            "/projects/:projectId/files",
            "/projects/:projectId/files/:fileid",
            "/projects/:projectId/messages",
            "/projects/:projectId/messages/:messageId",
            "/projects/:projectId/messages/:messageId/replies",
            "/projects/:projectId/messages/:messageId/replies/:replyId",
            "/projects/:projectId/permissions",
            "/projects/:projectId/permissions/:permissionId",
            "/users",
            "/users/:userId",
            "/users/:userId/messages",
            "/users/:userId/messages/:messageId",
            "/repos",
            "/repos/:repoId",
            "/repos/:repoId/pull_requests",
            "/repos/:repoId/pull_requests/:pullRequestId",
            "/repos/:repoId/pull_requests/:pullRequestId/comments",
            "/repos/:repoId/pull_requests/:pullRequestId/comments/:commentId",
            "/repos/:repoId/pull_requests/:pullRequestId/comments/:commentId/replies",
            "/repos/:repoId/pull_requests/:pullRequestId/comments/:commentId/replies/:replyId"
    };
    private static final int COUNT = 100000;
    private static final List<TestRequest> requests = new ArrayList<>();
    private static RouteMatcher routeMatcher1;

    @BeforeClass
    public static void setup() {
        routeMatcher1 = new RouteMatcher(new Configuration());

        for (String path : routePaths) {
            RouteImpl route = new RouteImpl("get", path, new Route() {
                @Override
                public void handle(Request request, Response response) {
                    response.send("test");
                }
            });
            routeMatcher1.addRoute("get", route);
        }

        final Random random = new Random();
        for (int i = 0; i < COUNT; i++) {
            TestRequest req = new TestRequest();
            req.path = routePaths[random.nextInt(routePaths.length)].replaceAll("\\/\\:\\w+", "/123");
            requests.add(i, req);
        }

        try {
            Thread.sleep(500);
        } catch (Exception e) {
        }
    }

    @Test
    public void doBenchmarkWithoutCache() {
        for (TestRequest req : requests) {
            List<RouteImpl> routes = routeMatcher1.getMatchingRoutes("get", req.path);
        }
    }

    private static class TestRequest {
        public String path;
    }
}
