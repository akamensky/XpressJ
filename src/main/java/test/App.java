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

import xpressj.XpressJ;

/**
 * Created by akamensky on 6/17/14.
 */
public class App {
    public static void main (String[] args){

        //Starting server
        XpressJ.start();

        //Adding first route
        XpressJ.get("/", (req, res) ->{
            if(req.getUri().equals("/")) {
                res.send(200, "Hello, World!");
            } else {
                res.send("The request has been made to uri '"+req.getUri()+"'");
            }
        });

        //Adding json route
        XpressJ.get("/getjson", (req, res) ->{
            res.json(new JsonTest());
        });

        //Adding second route
        XpressJ.get("/test", (req, res) ->{
            System.out.println("Executed lambda for route '/test'. The real requested route is '"+req.getUri()+"'");
        });

        XpressJ.get("*", (req, res) ->{
            System.out.println("Triggered on every request. Requested URI is '"+req.getUri()+"'");
        });

        XpressJ.get("/getj*", (req, res) ->{
            System.out.println("Will be triggered on /getj*");
        });

        XpressJ.get("/hello/*/world", (req, res) ->{
            System.out.println("Will be triggered on /hello/*/world");
        });

        XpressJ.get("/hello/test*/world", (req, res) ->{
            System.out.println("Will be triggered on /hello/test*/world");
        });
    }
}
