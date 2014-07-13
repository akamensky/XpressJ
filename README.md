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

        app.get("/", new Route() {
            @Override
            public void handle(Request request, Response response) {
                response.send("Hello World!");
            }
        });
    }
}
```
---
#### Development plans
---
#### Links
- [Development plans](https://trello.com/b/07AvGeym/xpressj)
