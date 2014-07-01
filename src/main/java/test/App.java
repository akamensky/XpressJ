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

package test;

import xpressj.*;

/**
 * Created by akamensky on 6/17/14.
 */
public class App {
    public static void main (String[] args){

        Configuration conf = new Configuration();

        //Starting server
        XpressJ.start(conf);

        //Adding first route
        XpressJ.get("/", new Route() {
            @Override
            public void handle(Request req, Response res) {
                if (req.getUri().equals("/")) {
                    res.send(200, "Hello, World!");
                } else {
                    res.send("The request has been made to uri '" + req.getUri() + "'");
                }
            }
        });

        //Adding json route
        XpressJ.get("/getjson", new Route() {
            @Override
            public void handle(Request req, Response res) {
                res.json(new JsonTest());
            }
        });

        //Adding second route
        XpressJ.get("/test", new Route() {
            @Override
            public void handle(Request req, Response res) {
                System.out.println("Executed lambda for route '/test'. The real requested route is '" + req.getUri() + "'");
            }
        });

        XpressJ.get("*", new Route() {
            @Override
            public void handle(Request req, Response res) {
                System.out.println("Triggered on every request. Requested URI is '" + req.getUri() + "'");
            }
        });

        XpressJ.get("/getj*", new Route() {
            @Override
            public void handle(Request req, Response res) {
                System.out.println("Will be triggered on /getj*");
            }
        });

        XpressJ.get("/hello/*/world", new Route() {
            @Override
            public void handle(Request req, Response res) {
                System.out.println("Will be triggered on /hello/*/world");
            }
        });

        XpressJ.get("/hello/test*/world", new Route() {
            @Override
            public void handle(Request req, Response res) {
                System.out.println("Will be triggered on /hello/test*/world");
            }
        });
    }
}
