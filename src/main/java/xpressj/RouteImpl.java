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

    protected RouteImpl(String httpMethod, String path, Route lambda){
        this.httpMethod = httpMethod;
        this.path = path;
        this.lambda = lambda;
        this.pathParts = new ArrayList<String>();

        if(path.length() == 1){
            if (path.equals("*")){
                this.pathParts.add(".*");
                hasWildcard = true;
            }
        } else {
            String[] parts = this.path.split("/");
            for (String part : parts) {
                if (part.endsWith("*")) {
                    part = part.substring(0, part.length() - 1) + ".*";
                    hasWildcard = true;
                }
                pathParts.add(part);
            }
        }
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
        //Method shortcut check
        if (!this.httpMethod.equals(httpMethod)){
            return false;
        }

        //Direct comparison shortcut
        if (!hasWildcard && !hasParameter && this.path.equals(path)){
            return true;
        }

        if (hasWildcard){
            String[] parts = null;
            if(path.equals("/")){
                parts = new String[]{"/"};
            } else {
                parts = path.split("/");
            }
            if (parts.length < pathParts.size()) {
                return false;
            }
            int counter = 0;
            boolean lastIsWildcard = false;
            for (String part : parts){
                //this element of route path exists
                if (counter <= pathParts.size()-1){
                    //If part doesn't have wildcard and doesn't match, then return false
                    if (!pathParts.get(counter).contains(".*")){
                        if (!pathParts.get(counter).equals(part)){
                            return false;
                        } else {
                            isMatching = true;
                            lastIsWildcard = false;
                        }
                    //If part has wildcard
                    } else {
                        //and it matches
                        if (part.matches(pathParts.get(counter))) {
                            //and that's last part, then return true
                            if (counter == pathParts.size() - 1) {
                                return true;
                                //and it's not last part, then we keep looping
                            } else {
                                isMatching = true;
                                lastIsWildcard = true;
                            }
                        //and it doesn't match, then return false
                        } else {
                            return false;
                        }
                    }
                } else {
                    if (lastIsWildcard){
                        return true;
                    } else {
                        return false;
                    }
                }
                counter++;
            }
        }

        return isMatching;
    }
}
