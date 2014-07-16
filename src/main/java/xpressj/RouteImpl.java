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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private Pattern routeRegex;
    private List<String> paramNames;

    protected RouteImpl(String httpMethod, String path, Route lambda){
        //TODO: optimize RouteImpl constructor
        this.httpMethod = httpMethod;
        this.path = path;
        this.lambda = lambda;
        this.paramNames = new ArrayList<>();

        //check if has wildcards
        if(this.path.matches("\\*")){
            hasWildcard = true;
        }

        //check if has parameters
        if (this.path.matches("[^:]+:[^/]+.*")) {
            hasParameter = true;
            //get param names
            String paramRegex = this.path;
            if (paramRegex.endsWith("*")){
                paramRegex = paramRegex.substring(0, paramRegex.lastIndexOf("*")) + ".+";
            }
            Pattern p = Pattern.compile(paramRegex.replaceAll("\\*", "[^/]+").replaceAll(":[^/]+", ":([^/]+)"));
            Matcher m = p.matcher(this.path);
            m.matches();
            if (m.groupCount() > 0) {
                for (int i = 1; i <= m.groupCount(); i++) {
                    paramNames.add(m.group(i));
                }
            }
        }

        //create regex string
        String tmp = this.path;
        String regex;
        if(this.path.endsWith("*")){
            tmp = tmp.substring(0, tmp.lastIndexOf("*")) + ".+";
        }
        regex = "^";
        regex += tmp.replaceAll("\\*", "[^/]+").replaceAll(":[^/]+", "([^/]+)");
        regex += "$";

        routeRegex = Pattern.compile(regex);
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
        //Extract params for this route
        Matcher matcher = routeRegex.matcher(req.getUri());
        matcher.matches();
        if (matcher.groupCount() > 0) {
            for (int i = 1; i <= matcher.groupCount(); i++) {
                req.addParam(paramNames.get(i - 1), matcher.group(i));
            }
        }

        //Execute route
        this.getLambda().handle(req, res);

        //Clean params after execution
        req.clearParams();
    }

    public boolean match(String httpMethod, String path){
        boolean isMatching = false;

        if (this.httpMethod != null && !this.httpMethod.equals(httpMethod)){
            return false;
        }

        Matcher matcher = routeRegex.matcher(path);

        if (matcher.matches()){
            isMatching = true;
        }

        return isMatching;
    }
}
