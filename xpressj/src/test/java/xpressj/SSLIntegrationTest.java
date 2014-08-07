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

import junit.framework.Assert;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import xpressj.route.Route;
import xpressj.server.Request;
import xpressj.server.Response;
import xpressj.util.TestUtil;

/**
 * Created by akamensky on 8/7/14.
 */
public class SSLIntegrationTest {

    static String keystoreLoc = "./xpressj/src/test/resources/keystore.jks";
    static String kystorePass = "password";

    static TestUtil testUtil;
    static XpressJ app;

    @AfterClass
    public static void stop() {
        app.stop();
    }

    @BeforeClass
    public static void start() {

        testUtil = new TestUtil(8081);

        app = new XpressJ(new Configuration().setPort(8081).setKeystoreFile(keystoreLoc).setKeystorePassword(kystorePass));

        app.get("/", new Route() {
            @Override
            public void handle(Request request, Response response) throws Exception {
                response.send(200, "test");
            }
        });
    }

    @Test
    public void testSSL() {
        try {
            TestUtil.UrlResponse response = testUtil.doMethodSecure("GET", "/", null);
            Assert.assertEquals(200, response.status);
            Assert.assertEquals("test", response.body);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
