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

import com.google.gson.Gson;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by akamensky on 6/19/14.
 */
public class Response {

    private boolean isConsumed = false;
    private HttpServletResponse httpResponse;
    private static int DEFAULT_STATUS_CODE = 200;
    private Integer statusCode;

    public Response(HttpServletResponse httpResponse) {
        this.httpResponse = httpResponse;
    }

    private void markConsumed(){
        this.isConsumed = true;
    }

    /**
     * True if response was already sent, no changes will affect HTTP response
     * @return boolean
     */
    public boolean isConsumed(){
        return this.isConsumed;
    }

    /**
     * Set status code for response. If not set DEFAULT_STATUS_CODE will be used.
     * @param status int Numeric HTTP response code
     */
    public void setStatusCode(int status){
        this.statusCode = status;
    }

    /**
     * Returns status code for HTTP response
     * @return int
     */
    public int getStatusCode(){
        if(this.statusCode == null){
            return DEFAULT_STATUS_CODE;
        } else {
            return this.statusCode;
        }
    }

    /**
     * Sends plain string response to client
     * @param code int status code
     * @param body string response body
     */
    public void send(int code, String body){
        setStatusCode(code);
        send(body);
    }

    /**
     * Sends plain string response to client
     * @param body String
     */
    public void send(String body) {
        try {
            httpResponse.setStatus(getStatusCode());
            httpResponse.setHeader("Content-Type", "text/html; charset=utf-8");
            httpResponse.getOutputStream().write(body.getBytes("utf-8"));
            markConsumed();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Sends plain string response to client
     * @param code int status code
     * @param obj Object
     */
    public void json(int code, Object obj){
        setStatusCode(code);
        json(obj);
    }

    /**
     * Transforms Object to JSON and sends JSON to client (using Googles GSON library)
     * @param obj Object
     */
    public void json(Object obj){
        Gson gson = new Gson();
        String json = gson.toJson(obj);
        try{
            httpResponse.setStatus(getStatusCode());
            httpResponse.setHeader("Content-Type", "application/json; charset=utf-8");
            httpResponse.getOutputStream().write(json.getBytes("utf-8"));
            markConsumed();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
