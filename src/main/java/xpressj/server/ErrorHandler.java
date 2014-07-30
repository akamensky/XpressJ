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

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.ResourceHandler;
import xpressj.Configuration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by akamensky on 7/30/14.
 */
public class ErrorHandler extends ResourceHandler {

    private Configuration configuration;

    public ErrorHandler(final Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException, ServletException {
        ResponseImpl res = new ResponseImpl(httpResponse);
        RequestImpl req = new RequestImpl(httpRequest, res, false);

        try {
            if (httpResponse.getStatus() == 500) {
                this.configuration.getErrorPage().handle(req, res);
            } else {
                this.configuration.getNotFoundPage().handle(req, res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            baseRequest.setHandled(true);
        }
    }
}
