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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by akamensky on 8/2/14.
 */
public class RequestImpl implements Request {
    public String getUri() {
        return null;
    }

    public String getHttpMethod() {
        return null;
    }

    public void addParam(String key, String value) {

    }

    public HashMap getParams() {
        return null;
    }

    public String getParam(String key) {
        return null;
    }

    public void clearParams() {

    }

    public int getParamsCount() {
        return 0;
    }

    public int getQueryParamsCount() {
        return 0;
    }

    public String[] getQueryParam(String key) {
        return new String[0];
    }

    public Map<String, String[]> getQueryParams() {
        return null;
    }

    public String[] getQueryParamsNames() {
        return new String[0];
    }

    public Map<String, Cookie> getCookies() {
        return null;
    }

    public Cookie getCookie(String name) {
        return null;
    }

    public String getHeader(String name) {
        return null;
    }

    public Collection<String> getHeaderNames() {
        return null;
    }

    public Map<String, String> getHeaders() {
        return null;
    }

    public File getFile(String name) {
        return null;
    }

    public Map<String, File> getFiles() {
        return null;
    }

    public Session getSession() {
        return null;
    }

    public void renewSession() {

    }
}
