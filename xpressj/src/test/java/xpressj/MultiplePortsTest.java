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

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import xpressj.route.Route;
import xpressj.server.Request;
import xpressj.server.Response;
import xpressj.util.TestUtil;

/**
 * Created by akamensky on 8/7/14.
 */
public class MultiplePortsTest {

    static TestUtil testUtil1;
    static TestUtil testUtil2;
    static XpressJ app;

    @AfterClass
    public static void stop() {
        app.stop();
    }

    @BeforeClass
    public static void start() {

        testUtil1 = new TestUtil(8081);
        testUtil2 = new TestUtil(8082);

        app = new XpressJ(new Configuration().setPorts(new int[]{8081, 8082}).enableSessions("TEST_SESS", 1000 * 60 * 60));

        app.start();

        app.get("/", new Route() {
            @Override
            public void handle(Request request, Response response) {
                response.send(200, "test");
            }
        });
    }

    @Test
    public void simple_session_persistence_test() {
        try {
            TestUtil.UrlResponse response1 = testUtil1.doMethod("GET", "/", null);
            TestUtil.UrlResponse response2 = testUtil2.doMethod("GET", "/", null);
            Assert.assertEquals(response1.status, 200);
            Assert.assertEquals(response2.status, 200);
            Assert.assertEquals(response1.body, "test");
            Assert.assertEquals(response2.body, "test");
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
