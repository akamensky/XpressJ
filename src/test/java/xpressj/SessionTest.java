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
import xpressj.server.Request;
import xpressj.server.Response;
import xpressj.server.Webserver;
import xpressj.util.TestUtil;

/**
 * Created by akamensky on 7/25/14.
 */
public class SessionTest {

    static TestUtil testUtil;
    static XpressJ app;
    static String cookieName = "TEST_SESS";
    static String sessionId;
    static String testObject = "Test String";

    @AfterClass
    public static void stop() {
        app.stop();
    }

    @BeforeClass
    public static void start() {

        testUtil = new TestUtil(8081);

        app = new XpressJ(new Configuration(Webserver.class).setPort(8081).enableSessions("TEST_SESS", 1000 * 60 * 60));

        app.start();

        app.get("/first", new Route() {
            @Override
            public void handle(Request request, Response response) {
                //First request to obtain session,
                //Set some object to session
                request.getSession().setProperty("test", testObject);
                response.send(200, "first");
            }
        });

        app.get("/second", new Route() {
            @Override
            public void handle(Request request, Response response) {
                //Second request to confirm session persistence
                if (request.getSession().getProperty("test") == testObject) {
                    response.send("match");
                } else {
                    response.send("no_match");
                }
            }
        });

    }

    @Test
    public void simple_session_persistence_test() {
        try {
            testUtil.doMethod("GET", "/first", null);
            TestUtil.UrlResponse response = testUtil.doMethod("GET", "/second", null);
            Assert.assertEquals(200, response.status);
            Assert.assertEquals("match", response.body);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

}
