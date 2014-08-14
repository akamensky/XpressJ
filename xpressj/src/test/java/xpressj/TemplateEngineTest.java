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

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by akamensky on 8/14/14.
 */
public class TemplateEngineTest {
    static TestUtil testUtil1;
    static TestUtil testUtil2;
    static XpressJ app1;
    static XpressJ app2;

    @AfterClass
    public static void stop() {
        app1.stop();
        app2.stop();
    }

    @BeforeClass
    public static void start() {
        //Copy file to tmp
        InputStream stream = TemplateEngineTest.class.getResourceAsStream("/templates/test.ftl");
        if (stream == null) {
            throw new RuntimeException("File test.ftl not found");
        }
        OutputStream resStreamOut;
        int readBytes;
        byte[] buffer = new byte[4096];
        try {
            resStreamOut = new FileOutputStream(new File("/tmp/test.ftl"));
            while ((readBytes = stream.read(buffer)) > 0) {
                resStreamOut.write(buffer, 0, readBytes);
            }
            stream.close();
            resStreamOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        testUtil1 = new TestUtil(8081);
        testUtil2 = new TestUtil(8082);

        app1 = new XpressJ(new Configuration().setPort(8081));
        app2 = new XpressJ(new Configuration().setPort(8082).setExternalTemplateLocation("/tmp"));

        app1.start();
        app2.start();

        app1.get("/", new Route() {
            @Override
            public void handle(Request request, Response response) {
                Map<String, Object> data = new HashMap<>();
                data.put("test", "test");
                response.render("test.ftl", data);
            }
        });

        app2.get("/", new Route() {
            @Override
            public void handle(Request request, Response response) {
                Map<String, Object> data = new HashMap<>();
                data.put("test", "test");
                response.render("test.ftl", data);
            }
        });
    }

    @Test
    public void embedded_templates_test() {
        try {
            TestUtil.UrlResponse response = testUtil1.doMethod("GET", "/", null);
            Assert.assertEquals(200, response.status);
            Assert.assertEquals("test", response.body);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void external_templates_test() {
        try {
            TestUtil.UrlResponse response = testUtil2.doMethod("GET", "/", null);
            Assert.assertEquals(200, response.status);
            Assert.assertEquals("test", response.body);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
