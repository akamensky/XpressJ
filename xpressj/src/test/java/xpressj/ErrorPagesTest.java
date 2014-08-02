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
 * Created by akamensky on 7/23/14.
 */
public class ErrorPagesTest {

    static TestUtil testUtil1;
    static TestUtil testUtil2;
    static XpressJ app1;
    static XpressJ app2;
    static Route customNotFoundPage = new Route() {
        @Override
        public void handle(Request request, Response response) throws Exception {
            response.send(200, "custom_message");
        }
    };
    static Route customErrorPage = new Route() {
        @Override
        public void handle(Request request, Response response) throws Exception {
            response.send(200, "all_good");
        }
    };

    @AfterClass
    public static void stop() {
        app1.stop();
        app2.stop();
    }

    @BeforeClass
    public static void start() {

        testUtil1 = new TestUtil(8081);
        testUtil2 = new TestUtil(8082);

        app1 = new XpressJ(new Configuration().setPort(8081));
        app2 = new XpressJ(new Configuration().setPort(8082).setNotFoundPage(customNotFoundPage).setErrorPage(customErrorPage));

        app1.start();
        app2.start();

        app1.get("/", new Route() {
            @Override
            public void handle(Request request, Response response) {
                response.send("test");
            }
        });
        app2.get("/", new Route() {
            @Override
            public void handle(Request request, Response response) {
                response.send("test");
            }
        });

        app1.get("/error", new Route() {
            @Override
            public void handle(Request request, Response response) throws Exception {
                throw new NullPointerException("test error");
            }
        });
        app2.get("/error", new Route() {
            @Override
            public void handle(Request request, Response response) throws Exception {
                throw new NullPointerException("test error");
            }
        });
    }

    @Test
    public void default_not_found_page_test() {
        try {
            TestUtil.UrlResponse response = testUtil1.doMethod("GET", "/test", null);
            Assert.assertEquals(404, response.status);
            Assert.assertEquals("Not Found", response.body);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void custom_not_found_page_test() {
        try {
            TestUtil.UrlResponse response = testUtil2.doMethod("GET", "/test", null);
            Assert.assertEquals(200, response.status);
            Assert.assertEquals("custom_message", response.body);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void default_error_page_test() {
        try {
            TestUtil.UrlResponse response = testUtil1.doMethod("GET", "/error", null);
            Assert.assertEquals(500, response.status);
            Assert.assertEquals("Internal Server Error", response.body);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void custom_error_page_test() {
        try {
            TestUtil.UrlResponse response = testUtil2.doMethod("GET", "/error", null);
            Assert.assertEquals(200, response.status);
            Assert.assertEquals("all_good", response.body);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

}
