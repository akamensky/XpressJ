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
import xpressj.server.WebserverImpl;
import xpressj.util.TestUtil;

import java.io.*;

/**
 * Created by akamensky on 7/1/14.
 */
public class GeneralIntegrationTest {

    static TestUtil testUtil;
    static XpressJ app;
    static File tmpFile;

    @AfterClass
    public static void stop() {
        app.stop();
        tmpFile.delete();
    }

    @BeforeClass
    public static void start() {

        testUtil = new TestUtil(8081);

        app = new XpressJ(new Configuration().setPort(8081).setStaticFilesLocation("/public").setExternalStaticFilesLocation("/tmp"));

        //Create file in /tmp
        tmpFile = new File("/tmp/tmp.txt");
        Writer writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tmpFile), "utf-8"));
            writer.write("tmp.txt");
        } catch (IOException e) {
            System.exit(100);
        } finally {
            try {
                writer.close();
            } catch (Exception e) {
            }
        }

        app.start();

        try {
            app.get("/", new Route() {
                @Override
                public void handle(Request request, Response response) {
                    response.send("get");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        app.post("/", new Route() {
            @Override
            public void handle(Request request, Response response) {
                response.send("post");
            }
        });

        app.options("/", new Route() {
            @Override
            public void handle(Request request, Response response) {
                response.send("options");
            }
        });

        app.head("/", new Route() {
            @Override
            public void handle(Request request, Response response) {
                response.send("head");
            }
        });

        app.put("/", new Route() {
            @Override
            public void handle(Request request, Response response) {
                response.send("put");
            }
        });

        app.delete("/", new Route() {
            @Override
            public void handle(Request request, Response response) {
                response.send("delete");
            }
        });

        app.trace("/", new Route() {
            @Override
            public void handle(Request request, Response response) {
                response.send("trace");
            }
        });

        app.all("/all", new Route() {
            @Override
            public void handle(Request request, Response response) {
                response.send("all");
            }
        });

        app.post("/fileupload", new Route() {
            @Override
            public void handle(Request request, Response response) {
                response.send(new String(request.getFile("file").getBytes()));
            }
        });
    }

    private static byte[] isToBytes(InputStream is) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            int nRead;
            byte[] data = new byte[1024];

            while ((nRead = is.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer.toByteArray();
    }

    @Test
    public void simple_get_route_test() {
        try {
            TestUtil.UrlResponse response = testUtil.doMethod("GET", "/", null);
            Assert.assertEquals(200, response.status);
            Assert.assertEquals("get", response.body);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void simple_post_route_test() {
        try {
            TestUtil.UrlResponse response = testUtil.doMethod("POST", "/", "Test2");
            Assert.assertEquals(200, response.status);
            Assert.assertEquals("post", response.body);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void simple_options_route_test() {
        try {
            TestUtil.UrlResponse response = testUtil.doMethod("OPTIONS", "/", null);
            Assert.assertEquals(200, response.status);
            Assert.assertEquals("options", response.body);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void simple_head_route_test() {
        try {
            TestUtil.UrlResponse response = testUtil.doMethod("HEAD", "/", null);
            Assert.assertEquals(200, response.status);
            Assert.assertEquals("", response.body);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void simple_put_route_test() {
        try {
            TestUtil.UrlResponse response = testUtil.doMethod("PUT", "/", "test2");
            Assert.assertEquals(200, response.status);
            Assert.assertEquals("put", response.body);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void simple_delete_route_test() {
        try {
            TestUtil.UrlResponse response = testUtil.doMethod("DELETE", "/", null);
            Assert.assertEquals(200, response.status);
            Assert.assertEquals("delete", response.body);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void simple_trace_route_test() {
        try {
            TestUtil.UrlResponse response = testUtil.doMethod("TRACE", "/", null);
            Assert.assertEquals(200, response.status);
            Assert.assertEquals("trace", response.body);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void simple_all_route_test() {
        try {
            TestUtil.UrlResponse response = testUtil.doMethod("GET", "/all", null);
            Assert.assertEquals(200, response.status);
            Assert.assertEquals("all", response.body);

            response = testUtil.doMethod("POST", "/all", "all");
            Assert.assertEquals(200, response.status);
            Assert.assertEquals("all", response.body);

            response = testUtil.doMethod("OPTIONS", "/all", null);
            Assert.assertEquals(200, response.status);
            Assert.assertEquals("all", response.body);

            response = testUtil.doMethod("HEAD", "/all", null);
            Assert.assertEquals(200, response.status);
            Assert.assertEquals("", response.body);

            response = testUtil.doMethod("PUT", "/all", "all");
            Assert.assertEquals(200, response.status);
            Assert.assertEquals("all", response.body);

            response = testUtil.doMethod("DELETE", "/all", null);
            Assert.assertEquals(200, response.status);
            Assert.assertEquals("all", response.body);

            response = testUtil.doMethod("TRACE", "/all", null);
            Assert.assertEquals(200, response.status);
            Assert.assertEquals("all", response.body);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void simple_static_bundled_file_test() {
        try {
            TestUtil.UrlResponse response = testUtil.doMethod("GET", "/test.txt", null);
            Assert.assertEquals(200, response.status);
            Assert.assertEquals("test.txt", response.body);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void simple_static_external_file_test() {
        try {
            TestUtil.UrlResponse response = testUtil.doMethod("GET", "/tmp.txt", null);
            Assert.assertEquals(200, response.status);
            Assert.assertEquals("tmp.txt", response.body);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void simple_file_upload_test() {
        try {
            String response = TestUtil.postFile("http://localhost:8081/fileupload", "file", tmpFile);
            Assert.assertEquals("tmp.txt", response);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

}
