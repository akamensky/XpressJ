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

package xpressj.util;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.BasicClientConnectionManager;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by akamensky on 7/1/14.
 */
public class TestUtil {

    private int port;

    private HttpClient httpClient;

    public TestUtil(int port) {
        this.port = port;
        Scheme http = new Scheme("http", port, PlainSocketFactory.getSocketFactory());
        SchemeRegistry sr = new SchemeRegistry();
        sr.register(http);
        ClientConnectionManager connMrg = new BasicClientConnectionManager(sr);
        this.httpClient = new DefaultHttpClient(connMrg);
    }

    public static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (Exception e) {
        }
    }

    public static String postFile(String url, String paramName, File file) {
        String response = "";

        HttpClient client = new DefaultHttpClient();
        client.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

        HttpPost post = new HttpPost(url);
        MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

        entity.addPart(paramName, new FileBody(file));

        post.setEntity(entity);

        try {
            response = EntityUtils.toString(client.execute(post).getEntity(), "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

        client.getConnectionManager().shutdown();

        return response;
    }

    public UrlResponse doMethod(String requestMethod, String path, String body) throws Exception {
        return doMethod(requestMethod, path, body, false, "text/html");
    }

    public UrlResponse doMethod(String requestMethod, String path, String body, String acceptType) throws Exception {
        return doMethod(requestMethod, path, body, false, acceptType);
    }

    private UrlResponse doMethod(String requestMethod, String path, String body, boolean secureConnection,
                                 String acceptType) throws Exception {

        HttpUriRequest httpRequest = getHttpRequest(requestMethod, path, body, secureConnection, acceptType);
        HttpResponse httpResponse = httpClient.execute(httpRequest);

        UrlResponse urlResponse = new UrlResponse();
        urlResponse.status = httpResponse.getStatusLine().getStatusCode();
        HttpEntity entity = httpResponse.getEntity();
        if (entity != null) {
            urlResponse.body = EntityUtils.toString(entity);
        } else {
            urlResponse.body = "";
        }
        Map<String, String> headers = new HashMap<String, String>();
        Header[] allHeaders = httpResponse.getAllHeaders();
        for (Header header : allHeaders) {
            headers.put(header.getName(), header.getValue());
        }
        urlResponse.headers = headers;
        return urlResponse;
    }

    private HttpUriRequest getHttpRequest(String requestMethod, String path, String body, boolean secureConnection,
                                          String acceptType) {
        try {
            String protocol = secureConnection ? "http" : "http";
            String uri = protocol + "://localhost:" + port + path;

            if (requestMethod.equals("GET")) {
                HttpGet httpGet = new HttpGet(uri);

                httpGet.setHeader("Accept", acceptType);

                return httpGet;
            }

            if (requestMethod.equals("POST")) {
                HttpPost httpPost = new HttpPost(uri);
                httpPost.setHeader("Accept", acceptType);
                httpPost.setEntity(new StringEntity(body));
                return httpPost;
            }

            if (requestMethod.equals("PATCH")) {
                HttpPatch httpPatch = new HttpPatch(uri);
                httpPatch.setHeader("Accept", acceptType);
                httpPatch.setEntity(new StringEntity(body));
                return httpPatch;
            }

            if (requestMethod.equals("DELETE")) {
                HttpDelete httpDelete = new HttpDelete(uri);
                httpDelete.setHeader("Accept", acceptType);
                return httpDelete;
            }

            if (requestMethod.equals("PUT")) {
                HttpPut httpPut = new HttpPut(uri);
                httpPut.setHeader("Accept", acceptType);
                httpPut.setEntity(new StringEntity(body));
                return httpPut;
            }

            if (requestMethod.equals("HEAD")) {
                return new HttpHead(uri);
            }

            if (requestMethod.equals("TRACE")) {
                return new HttpTrace(uri);
            }

            if (requestMethod.equals("OPTIONS")) {
                return new HttpOptions(uri);
            }

            throw new IllegalArgumentException("Unknown method " + requestMethod);

        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

    }

    public int getPort() {
        return port;
    }

    public static class UrlResponse {

        public Map<String, String> headers;
        public String body;
        public int status;
    }

}