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

package xpressj.server;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.util.*;

/**
 * Created by akamensky on 7/30/14.
 */
public class RequestImpl implements Request {

    private String uri;
    private String httpMethod;
    private HashMap<String, String> params;
    private Map<String, String[]> query;
    private Map<String, Cookie> cookies;
    private Map<String, String> headers;
    private Map<String, File> files;
    private Session session;
    private String sessionCookieName;
    private ResponseImpl response;

    public RequestImpl(HttpServletRequest httpRequest, Response response, boolean isMultipart) {
        this(httpRequest, response, isMultipart, null);
    }

    public RequestImpl(HttpServletRequest httpRequest, Response response, boolean isMultipart, SessionFactory sessionFactory) {
        this.uri = httpRequest.getRequestURI();
        this.httpMethod = httpRequest.getMethod().toLowerCase();
        this.params = new HashMap<>();
        this.query = httpRequest.getParameterMap();
        //get cookies
        javax.servlet.http.Cookie[] cookies = httpRequest.getCookies();
        if (cookies != null) {
            this.cookies = Collections.unmodifiableMap(CookieImpl.toMap(cookies));
        } else {
            this.cookies = Collections.unmodifiableMap(new HashMap<String, Cookie>());
        }

        //deal with session
        if (sessionFactory != null) {
            this.sessionCookieName = sessionFactory.getSessionCookieName();
            boolean needNewSession = true;
            Cookie sessionCookie = this.cookies.get(this.sessionCookieName);
            //TODO: move session cookie name somewhere else (config?)
            if (sessionCookie != null) {
                Session session = sessionFactory.getSession(sessionCookie.getValue());
                if (session != null) {
                    this.session = session;
                    needNewSession = false;
                }
            }

            if (needNewSession) {
                Session session = sessionFactory.createSession();
                this.session = session;
                sessionCookie = new CookieImpl(this.sessionCookieName, session.getId(), session.getCookieMaxAge());
                response.addCookie(sessionCookie);
            }
        }

        //get headers
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = httpRequest.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.put(headerName, httpRequest.getHeader(headerName));
        }
        this.headers = Collections.unmodifiableMap(headers);

        //fill files if any
        this.files = new HashMap<>();
        if (isMultipart) {
            try {
                Collection<Part> parts = httpRequest.getParts();
                for (Part part : parts) {
                    if (part.getSubmittedFileName() != null) {
                        this.files.put(part.getName(), new FileImpl(part));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.files = Collections.unmodifiableMap(this.files);

        return;
    }

    public RequestImpl(String httpMethod, String uri) {
        this.uri = uri;
        this.httpMethod = httpMethod;
        this.params = new HashMap<>();
        this.cookies = new HashMap<>();
    }

    ;

    public void setDelegate(ResponseImpl response) {
        this.response = response;
    }

    public String getUri() {
        return this.uri;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void addParam(String key, String value) {
        this.params.put(key, value);
    }

    public HashMap getParams() {
        return this.params;
    }

    public String getParam(String key) {
        return this.params.get(key);
    }

    public void clearParams() {
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

    public Map<String, String[]> getQueryParams() {
        return this.query;
    }

    public String[] getQueryParamsNames() {
        Object[] obj = this.query.keySet().toArray();
        String[] str = Arrays.copyOf(obj, obj.length, String[].class);
        return str;
    }

    public Map<String, Cookie> getCookies() {
        return this.cookies;
    }

    public Cookie getCookie(String name) {
        return this.cookies.get(name);
    }

    public String getHeader(String name) {
        return this.headers.get(name);
    }

    public Collection<String> getHeaderNames() {
        return this.headers.keySet();
    }

    public Map<String, String> getHeaders() {
        return this.headers;
    }

    public File getFile(String name) {
        return this.files.get(name);
    }

    public Map<String, File> getFiles() {
        return this.files;
    }

    public Session getSession() {
        return this.session;
    }

    public void renewSession() {
        if (this.session == null) {
            return;
        }

        this.session.reset();
        int maxAge = this.session.getMaxAge();
        this.response.addCookie(this.sessionCookieName, this.session.getId());
        //TODO: move session cookie name somewhere else (config?)
    }

    public void close() {
        for (File file : this.files.values()) {
            file.delete();
        }
    }
}
