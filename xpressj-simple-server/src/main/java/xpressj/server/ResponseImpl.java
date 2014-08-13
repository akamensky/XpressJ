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

import java.io.OutputStream;
import java.util.Map;

/**
 * Created by akamensky on 8/2/14.
 */
public class ResponseImpl implements Response {

    private OutputStream outputStream;
    private RequestImpl request;

    public ResponseImpl(OutputStream out) {
        this.outputStream = out;
    }

    public void setRequest(RequestImpl request) {
        this.request = request;
    }

    public boolean isConsumed() {
        return false;
    }

    public int getStatusCode() {
        return 0;
    }

    public void setStatusCode(int status) {

    }

    public void send(int code, String body) {
        try {
            this.outputStream.write(body.getBytes());
            this.outputStream.flush();
            this.outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void send(String body) {
        this.send(200, body);
    }

    public void json(int code, Object obj) {

    }

    public void json(Object obj) {

    }

    public void render(int code, String templateName, Map<String, Object> obj) {

    }

    public void render(String templateName, Map<String, Object> obj) {

    }

    public void addCookie(Cookie cookie) {

    }

    public void addCookie(String name, String value) {

    }

    public void unsetCookie(String name) {

    }

    public void addHeader(String name, String value) {

    }
}