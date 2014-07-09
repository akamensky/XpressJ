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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by akamensky on 7/8/14.
 */
public class Cookie {

    private String name;
    private String value;
    private String path;
    private int maxAge;
    private boolean isSecure = false;
    private boolean isHttpOnly = false;

    public Cookie(javax.servlet.http.Cookie httpCookie){
        this.name = httpCookie.getName();
        this.value = httpCookie.getValue();
        this.path = httpCookie.getPath();
        this.maxAge = httpCookie.getMaxAge();
        this.isSecure = httpCookie.getSecure();
        this.isHttpOnly = httpCookie.isHttpOnly();
    }

    public Cookie(String name, String value, int maxAge, String path, boolean isSecure, boolean isHttpOnly){
        this.name = name;
        this.value = value;
        this.path = path;
        this.maxAge = maxAge;
        this.isSecure = isSecure;
        this.isHttpOnly = isHttpOnly;
    }

    public Cookie(String name, String value){
        this.name = name;
        this.value = value;
    }

    public Cookie(String name, String value, int maxAge){
        this.name = name;
        this.value = value;
        this.maxAge = maxAge;
    }

    public Cookie(String name, String value, int maxAge, String path){
        this.name = name;
        this.value = value;
        this.maxAge = maxAge;
        this.path = path;
    }

    public static Map<String, Cookie> toMap(javax.servlet.http.Cookie[] cookies){
        Map<String, Cookie> map = new HashMap<String, Cookie>();
        for (javax.servlet.http.Cookie cookie : cookies){
            Cookie newCookie = new Cookie(cookie);
            map.put(newCookie.getName(), newCookie);
        }
        return map;
    }

    public String getName(){
        return this.name;
    }

    public String getValue(){
        return this.value;
    }

    public javax.servlet.http.Cookie getServletCookie(){
        javax.servlet.http.Cookie cookie = new javax.servlet.http.Cookie(this.name, this.value);
        if (this.path != null){
            cookie.setPath(this.path);
        }
        if (this.maxAge > 0){
            cookie.setMaxAge(this.maxAge);
        }
        cookie.setHttpOnly(this.isHttpOnly);
        cookie.setSecure(this.isSecure);
        return cookie;
    }

    public String toString(){
        return this.value;
    }
}
