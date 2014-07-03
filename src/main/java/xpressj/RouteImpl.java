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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akamensky on 6/19/14.
 */
public class RouteImpl {

    private String path;
    private Route lambda;
    private String httpMethod;
    private List<String> pathParts;
    private boolean hasWildcard = false;
    private boolean hasParameter = false;
    private String routeRegex;

    protected RouteImpl(String httpMethod, String path, Route lambda){
        this.httpMethod = httpMethod;
        this.path = path;
        this.lambda = lambda;

        //check if has wildcards
        if(this.path.matches("\\*")){
            hasWildcard = true;
        }

        //check if has parameters
        if (this.path.matches("[^:]+:[^/]+.*")){
            hasParameter = true;
        }

        //create regex string
        String tmp = this.path;
        if(this.path.endsWith("*")){
            tmp = tmp.substring(0, tmp.lastIndexOf("*")) + ".+";
        }
        this.routeRegex = "^";
        this.routeRegex += tmp.replaceAll("\\*", "[^/]+").replaceAll(":[^/]+", "[^/]+");
        this.routeRegex += "$";
    }

    public String getHttpMethod(){
        return this.httpMethod;
    }

    public String getPath(){
        return this.path;
    }

    private Route getLambda(){
        return this.lambda;
    }

    public void handle(Request req, Response res){
        //TODO: Add parameters handling
        this.getLambda().handle(req, res);
    }

    public boolean match(String httpMethod, String path){
        boolean isMatching = false;

        if (!this.httpMethod.equals(httpMethod)){
            return false;
        }

        if (path.matches(this.routeRegex)){
            isMatching = true;
        }

        return isMatching;
    }
}
