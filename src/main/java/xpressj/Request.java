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

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by akamensky on 6/19/14.
 */
public class Request {

    private String uri;
    private String httpMethod;
    private HashMap<String, String> params;
    private Map<String, String[]> query;
    private Map<String, Cookie> cookies;
    private Map<String, String> headers;

    public Request(HttpServletRequest httpRequest) {
        this.uri = httpRequest.getRequestURI();
        this.httpMethod = httpRequest.getMethod().toLowerCase();
        this.params = new HashMap<>();
        this.query = httpRequest.getParameterMap();
        //get cookies
        javax.servlet.http.Cookie[] cookies = httpRequest.getCookies();
        if(cookies != null) {
            this.cookies = Collections.unmodifiableMap(Cookie.toMap(cookies));
        } else {
            this.cookies = Collections.unmodifiableMap(new HashMap<String, Cookie>());
        }

        //get headers
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = httpRequest.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.put(headerName, httpRequest.getHeader(headerName));
        }
        this.headers = Collections.unmodifiableMap(headers);

        return;
    }

    public Request(String httpMethod, String uri){
        this.uri = uri;
        this.httpMethod = httpMethod;
        this.params = new HashMap<>();
        this.cookies = new HashMap<>();
    };

    public String getUri(){
        return this.uri;
    }

    public String getHttpMethod(){
        return httpMethod;
    }

    public void addParam(String key, String value){
        this.params.put(key, value);
    }

    public HashMap getParams(){
        return this.params;
    }

    public String getParam(String key){
        return this.params.get(key);
    }

    public void clearParams(){
        this.params = new HashMap<>();
    }

    public int getParamsCount() {
        return this.params.size();
    }

    public int getQueryParamsCount() {
        return this.query.size();
    }

    public String[] getQueryParam(String key) {
        return this.query.get(key);
    }

    public Map<String, String[]> getQueryParams(){
        return this.query;
    }

    public String[] getQueryParamsNames(){
        Object[] obj = this.query.keySet().toArray();
        String[] str = Arrays.copyOf(obj, obj.length, String[].class);
        return str;
    }

    public Map<String, Cookie> getCookies(){
        return this.cookies;
    }

    public Cookie getCookie(String name) {
        return this.cookies.get(name);
    }

    public String getHeader(String name){
        return this.headers.get(name);
    }

    public Collection<String> getHeaderNames(){
        return this.headers.keySet();
    }

    public Map<String, String> getHeaders(){
        return this.headers;
    }
}
