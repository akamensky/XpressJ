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

/**
 * Created by akamensky on 6/30/14.
 */
public class Configuration {
    private static final int DEFAULT_PORT = 8080;
    private static final String DEFAULT_HOST = "0.0.0.0";

    private int port = DEFAULT_PORT;
    private String host = DEFAULT_HOST;

    private boolean cacheEnabled = false;

    public Configuration setPort(int port){
        this.port = port;
        return this;
    }

    public int getPort(){
        return this.port;
    }

    public Configuration setHost(String host){
        this.host = host;
        return this;
    }

    public String getHost(){
        return this.host;
    }

    public Configuration setRouteCache(boolean flag){
        this.cacheEnabled = flag;
        return this;
    }

    public boolean isCacheEnabled(){
        return this.cacheEnabled;
    }
}
