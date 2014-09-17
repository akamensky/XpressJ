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

import java.util.HashMap;
import java.util.Map;

/**
 * Created by akamensky on 7/30/14.
 */
public class SessionImpl implements Session {
    private String id;
    private int maxAge;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                Thread.sleep(maxAge);
                factory.expireSession(id);
            } catch (InterruptedException e) {
                //do nothing
            }
        }
    };
    private int cookieMaxAge;
    private SessionFactory factory;
    private Thread t;
    private Map<String, Object> properties;

    public SessionImpl(String id, int maxAge, SessionFactory factory) {
        this.id = id;
        this.maxAge = (maxAge == 0) ? 1000 * 60 * 60 * 24 : maxAge;
        this.cookieMaxAge = maxAge;
        this.factory = factory;
        this.properties = new HashMap<>();

        t = new Thread(runnable);
        t.start();
    }

    public void reset() {
        reset(maxAge);
    }

    public void reset(int milliseconds) {
        this.maxAge = milliseconds;
        t.interrupt();
        t = new Thread(runnable);
        t.start();
    }

    public Object getProperty(String name) {
        return this.properties.get(name);
    }

    public void setProperty(String name, Object propertyObject) {
        this.properties.put(name, propertyObject);
    }

    public void unsetProperty(String name) {
        this.properties.remove(name);
    }

    public int getMaxAge() {
        return this.maxAge;
    }

    public int getCookieMaxAge() {
        return this.cookieMaxAge;
    }

    public String getId() {
        return this.id;
    }
}
