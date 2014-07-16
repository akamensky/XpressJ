![XpressJ](https://raw.githubusercontent.com/akamensky/XpressJ/master/graphics/xpressj-200x200.png)
# XpressJ

[![Build Status](https://travis-ci.org/akamensky/XpressJ.svg?branch=master)](https://travis-ci.org/akamensky/XpressJ)

#### Table of contents
- [Introduction](#introduction)
- [Examples](#examples)
- [Development plans](#development-plans)
- [Links](#links)

---
#### Introduction
[__XpressJ__](https://github.com/akamensky/XpressJ) is a lightweight framework for building standalone (for now) Java web applications. It is inspired by JavaScript framework [__Express.js__](https://github.com/visionmedia/express) which was initially based on Sinatra. There is already great framework the goal of which is to 100% reproduce Sinatra functionality for Java web applications. You may want to look at it - [__Spark__](https://github.com/perwendel/spark). However while trying to use it I found some limitations or rather disadvantages (IMO) comparing to Express.js. Therefore XpressJ is my attempt to bring same logic into Java allowing fast development and great extendability. _This project is still under heavy development and is not recommended (yet) for production use. First production-ready release will be of version 1.0.0 and there is a long way to go before that._ __Not released to Maven Central yet.__

---
#### Examples
Simple HelloWorld application:
```java
import xpressj.*;

public class HelloWorld {
    public static void main (String[] args){
        XpressJ app = new XpressJ(new Configuration());

        app.start();

        app.get("/", (request, response) -> {
            response.send("Hello World!");
        });
    }
}
```
Use of wildcards:
```java
app.get("*", new Route() {
    @Override
    public void handle(Request request, Response response) {
        //Will match every GET request
    }
});

app.get("/hello/*", new Route() {
    @Override
    public void handle(Request request, Response response) {
        //Will match every GET request under path /hello/...
    }
});

app.get("/hello/*/", new Route() {
    @Override
    public void handle(Request request, Response response) {
        //Will match /hello/world/ and /hello/people/ etc
    }
});

app.get("/hello/*/world", new Route() {
    @Override
    public void handle(Request request, Response response) {
        //Will match /hello/new/world and /hello/old/world etc
    }
});
```
Use of parameters:
```java
app.get("/hello/:param", new Route() {
    @Override
    public void handle(Request request, Response response) {
        //Will match only for URI with two parts and respond with second part
        response.send(request.getParam("param"));
    }
});

app.get("/topics/topic-:param", new Route() {
    @Override
    public void handle(Request request, Response response) {
        //Will match only for URI with two parts and respond with substitution for :param
        response.send(request.getParam("param"));
    }
});
```
For more, please use documentation (WiP)

---
#### Development plans
- ~~0.2.0~~
    - ~~Add all verbs from HTTP 1.1 specification~~
    - ~~Add "all" verb to match any of HTTP verbs~~
    - ~~Validate route URI on application start (when adding routes)~~
- __0.3.0__
    - Add file handling (as bytes array)
- 0.4.0
    - Add session handling
- 0.5.0
    - Separate Jetty server into external dependency xpressj-jetty perhaps
- 1.0.0
    - Implement HTTPS protocol
    - ...

---
#### Links
- [Development plans](https://trello.com/b/07AvGeym/xpressj)
- [Website (WiP)](http://xpressj.com/)
- [Javadoc (WiP)](http://xpressj.com/javadoc)
