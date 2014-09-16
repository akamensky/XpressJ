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

import com.google.gson.Gson;
import xpressj.template.Template;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by akamensky on 7/30/14.
 */
public class ResponseImpl implements Response {

    private static int DEFAULT_STATUS_CODE = 200;
    private boolean isConsumed = false;
    private HttpServletResponse httpResponse;
    private Integer statusCode;
    private HashMap<String, Cookie> cookies;
    private Map<String, String> headers;
    private RequestImpl request;
    private Template templateEngine;

    public ResponseImpl(HttpServletResponse httpResponse) {
        this.httpResponse = httpResponse;
        this.cookies = new HashMap<>();
        this.headers = new HashMap<>();
    }

    public ResponseImpl() {
    }

    public void setDelegate(RequestImpl request) {
        this.request = request;
    }

    private void markConsumed() {
        this.isConsumed = true;
    }

    /**
     * True if response was already sent, no changes will affect HTTP response
     *
     * @return boolean
     */
    public boolean isConsumed() {
        return this.isConsumed;
    }

    /**
     * Returns status code for HTTP response
     *
     * @return int
     */
    public int getStatusCode() {
        if (this.statusCode == null) {
            return DEFAULT_STATUS_CODE;
        } else {
            return this.statusCode;
        }
    }

    /**
     * Set status code for response. If not set DEFAULT_STATUS_CODE will be used.
     *
     * @param status int Numeric HTTP response code
     */
    public void setStatusCode(int status) {
        this.statusCode = status;
    }

    /**
     * Sends plain string response to client
     *
     * @param code int status code
     * @param body string response body
     */
    public void send(int code, String body) {
        setStatusCode(code);
        send(body);
    }

    /**
     * Sends plain string response to client
     *
     * @param body String
     */
    public void send(String body) {
        writeResponse(body);
    }

    private void writeResponse(String body) {
        try {
            if (this.isConsumed) {
                throw new RuntimeException("Request has already been consumed");
            }
            httpResponse.setStatus(getStatusCode());
            httpResponse.setHeader("Content-Type", "text/html; charset=utf-8");

            //Adding cookies to the response
            for (Cookie cookie : this.cookies.values()) {
                httpResponse.addCookie(((CookieImpl) cookie).getServletCookie());
            }

            //Adding headers to the response
            Collection<String> headerNames = this.headers.keySet();
            for (String headerName : headerNames) {
                httpResponse.setHeader(headerName, this.headers.get(headerName));
            }

            httpResponse.getOutputStream().write(body.getBytes("utf-8"));
            markConsumed();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends plain string response to client
     *
     * @param code int status code
     * @param obj  Object
     */
    public void json(int code, Object obj) {
        setStatusCode(code);
        json(obj);
    }

    /**
     * Transforms Object to JSON and sends JSON to client (using Googles GSON library)
     *
     * @param obj Object
     */
    public void json(Object obj) {
        Gson gson = new Gson();
        String json = gson.toJson(obj);
        writeResponse(json);
    }

    /**
     *
     * @param code HTTP response code
     * @param templateName name of template file
     * @param obj data-model object
     */
    public void render(int code, String templateName, Map<String, Object> obj) {
        setStatusCode(code);
        this.render(templateName, obj);
    }

    public void render(String templateName, Map<String, Object> obj) {
        String result = this.templateEngine.process(templateName, obj);
        writeResponse(result);
    }

    public void addCookie(Cookie cookie) {
        this.cookies.put(cookie.getName(), cookie);
    }

    public void addCookie(String name, String value) {
        Cookie cookie = new CookieImpl(name, value);
        addCookie(cookie);
    }

    public void unsetCookie(String name) {
        this.cookies.remove(name);
    }

    public void addHeader(String name, String value) {
        this.headers.put(name, value);
    }

    public void setTemplateEngine(Template templateEngine) {
        this.templateEngine = templateEngine;
    }

    public void file(java.io.File file) {
        try {
            this.file(file.getName(), new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void file(String filename, InputStream stream) {
        try {
            if (this.isConsumed) {
                throw new RuntimeException("Request has already been consumed");
            }
            httpResponse.setStatus(getStatusCode());
            httpResponse.setHeader("Content-Type", "application/octet-stream");
            if (filename != null && !filename.isEmpty()) {
                httpResponse.setHeader("Content-disposition", "attachment; filename=\"" + filename + "\"");
            } else {
                httpResponse.setHeader("Content-disposition", "attachment; filename=\"\"");
            }

            //Adding cookies to the response
            for (Cookie cookie : this.cookies.values()) {
                httpResponse.addCookie(((CookieImpl) cookie).getServletCookie());
            }

            //Adding headers to the response
            Collection<String> headerNames = this.headers.keySet();
            for (String headerName : headerNames) {
                httpResponse.setHeader(headerName, this.headers.get(headerName));
            }

            //Writing content to response
            byte[] buffer = new byte[1024*100];//100KB buffer
            int len;
            while ((len = stream.read(buffer)) != -1) {
                httpResponse.getOutputStream().write(buffer, 0, len);
            }

            markConsumed();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void redirect(int code, String location){
        setStatusCode(code);
        this.addHeader("Location", location);
        writeResponse("");
    }
}
