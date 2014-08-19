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

package xpressj.example;

import xpressj.Configuration;
import xpressj.XpressJ;
import xpressj.route.Route;
import xpressj.server.Request;
import xpressj.server.Response;

import java.util.HashMap;
/**
 * Created by akamensky on 7/2/14.
 */
public class App {

    public static void main(String[] args) {
        XpressJ app = new XpressJ(new Configuration().setStaticFilesLocation("/public").enableSessions());
        app.start();

        //Match GET requests to "/"
        app.get("/", new Route() {
            @Override
            public void handle(Request request, Response response) {
                response.render("index.html", new HashMap<String, Object>());
            }
        });
    }

}
