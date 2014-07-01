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

import org.junit.*;
import xpressj.util.TestUtil;

import xpressj.*;

/**
 * Created by akamensky on 7/1/14.
 */
public class GeneralIntegrationTest {

    static TestUtil testUtil;

    @After
    public void stop(){
        XpressJ.stop();
    }

    @Before
    public void start(){

        testUtil = new TestUtil(8080);

        XpressJ.start(new Configuration());

        XpressJ.get("/", new Route() {
            @Override
            public void handle(Request request, Response response) {
                response.send("Test1");
            }
        });

        XpressJ.get("/test2", new Route() {
            @Override
            public void handle(Request request, Response response) {
                response.send(200, "Test2");
            }
        });

    }

    @Test
    public void simple_get_route_test() {
        try{
            TestUtil.UrlResponse response = testUtil.doMethod("GET", "/", null);
            Assert.assertEquals(200, response.status);
            Assert.assertEquals("Test1", response.body);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
