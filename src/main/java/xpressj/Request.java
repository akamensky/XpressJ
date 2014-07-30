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

import javax.servlet.http.Part;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by akamensky on 7/30/14.
 */
public interface Request {
    public String getUri();
    public String getHttpMethod();
    public void addParam(String key, String value);
    public HashMap getParams();
    public String getParam(String key);
    public void clearParams();
    public int getParamsCount();
    public int getQueryParamsCount();
    public String[] getQueryParam(String key);
    public Map<String, String[]> getQueryParams();
    public String[] getQueryParamsNames();
    public Map<String, Cookie> getCookies();
    public Cookie getCookie(String name);
    public String getHeader(String name);
    public Collection<String> getHeaderNames();
    public Map<String, String> getHeaders();
    public Part getFile(String name);//TODO: Should return InputStream instead of Part
    public Map<String, Part> getFiles();//TODO: Should return InputStream instead of Part
    public Session getSession();
    public void renewSession();
}
