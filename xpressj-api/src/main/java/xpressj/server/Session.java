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
public interface Session {
    public void reset();

    public void reset(int milliseconds);

    public Object getProperty(String name);

    public void setProperty(String name, Object propertyObject);

    public int getMaxAge();

    public int getCookieMaxAge();

    public String getId();
}