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

/**
 * Created by akamensky on 7/30/14.
 */
public interface Response {
    public boolean isConsumed();

    public int getStatusCode();

    public void setStatusCode(int status);

    public void send(int code, String body);

    public void send(String body);

    public void json(int code, Object obj);

    public void json(Object obj);

    public void addCookie(Cookie cookie);//TODO: This is wrong, just add more methods for adding cookie with maxAge etc.

    public void addCookie(String name, String value);

    public void unsetCookie(String name);

    public void addHeader(String name, String value);
}
