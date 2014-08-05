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

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by akamensky on 8/2/14.
 */
public class RequestImpl implements Request {

    private String method;
    private String URI;
    private String protocol;
    private Map<String, String> headers;
    private Map<String, String> cookies;

    private boolean hasBody;
    private boolean isMultipart;

    private String requestString;
    private byte[] requestBytes;

    private ResponseImpl response;

    private class Multipart{
        ArrayList<String> lines = new ArrayList<>();
    }

    public RequestImpl(InputStream in) {
        this.headers = new HashMap<>();
        this.cookies = new HashMap<>();

        //Read and parse request
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        //parse headers
        //unconditional as headers must be present
        this.parseHeaders(reader, buffer);

        //parse body
        //conditional depending on appropriate headers
        if (this.hasBody && !this.isMultipart) {
            this.parseBody(reader, buffer);
        } else if (this.hasBody && this.isMultipart){
            this.parseMultipart(reader, buffer);
        }

        //Write raw presentation of request as bytes
        //and request content as string
        //TODO:don't need content request as string I guess
        try {
            buffer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.requestBytes = buffer.toByteArray();
        this.requestString = new String(this.requestBytes);
    }

    public void setResponse(ResponseImpl response) {
        this.response = response;
        response.send(this.requestString);
    }

    private void parseHeaders(BufferedReader reader, ByteArrayOutputStream buffer){
        String inputLine;
        try {
            while ((inputLine = reader.readLine()) != null) {
                buffer.write((inputLine+"\r\n").getBytes());
                if (this.method == null) {
                    inputLine = inputLine.trim();
                    String[] parts = inputLine.split(" ");
                    if (parts.length < 3) {
                        throw new RuntimeException("Malformed request");
                    } else {
                        this.method = parts[0].toUpperCase();
                        this.URI = parts[1];
                        this.protocol = parts[2];
                    }
                } else if (!inputLine.isEmpty()) {
                    String[] parts = inputLine.split(": ");
                    this.headers.put(parts[0], parts[1]);
                } else {
                    //detect request body
                    if (this.headers.containsKey("Content-Length") && Integer.parseInt(this.headers.get("Content-Length")) > 0) {
                        this.hasBody = true;
                    }
                    //detect if request is multipart
                    if (this.headers.containsKey("Content-Type") && this.headers.get("Content-Type").startsWith("multipart/") && this.headers.get("Content-Type").contains("boundary=")) {
                        this.isMultipart = true;
                    }
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseBody(BufferedReader reader, ByteArrayOutputStream buffer) {
        //read body
        try {
            char[] cbuf = new char[Integer.parseInt(this.headers.get("Content-Length"))];
            reader.read(cbuf, 0, Integer.parseInt(this.headers.get("Content-Length")));
            String str = new String(cbuf);
            buffer.write(str.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //parse body
        //TODO: parse request body!
    }

    private void parseMultipart(BufferedReader reader, ByteArrayOutputStream buffer){
        //TODO: handle malformed requests!!!
        //find boundary
        String boundary = this.headers.get("Content-Type").substring(this.headers.get("Content-Type").indexOf("boundary=") + "boundary=".length());

        //start parsing
        String inputLine;
        ArrayList<Multipart> parts = new ArrayList<>();
        Multipart part = new Multipart();
        try {
            while ((inputLine = reader.readLine()) != null) {
                buffer.write((inputLine+"\r\n").getBytes());
                if (inputLine.equals("--"+boundary)) {
                    //new part started
                    if (!part.lines.isEmpty()) {
                        parts.add(part);
                    }
                    part = new Multipart();
                } else if (inputLine.equals("--"+boundary+"--")) {
                    //all of them ended
                    if (!part.lines.isEmpty()) {
                        parts.add(part);
                    }
                    break;
                } else {
                    //Add line to multipart object
                    part.lines.add(inputLine);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //parse parts
        //TODO: parse parts
    }

    public String getUri() {
        return null;
    }

    public String getHttpMethod() {
        return null;
    }

    public void addParam(String key, String value) {

    }

    public HashMap getParams() {
        return null;
    }

    public String getParam(String key) {
        return null;
    }

    public void clearParams() {

    }

    public int getParamsCount() {
        return 0;
    }

    public int getQueryParamsCount() {
        return 0;
    }

    public String[] getQueryParam(String key) {
        return new String[0];
    }

    public Map<String, String[]> getQueryParams() {
        return null;
    }

    public String[] getQueryParamsNames() {
        return new String[0];
    }

    public Map<String, Cookie> getCookies() {
        return null;
    }

    public Cookie getCookie(String name) {
        return null;
    }

    public String getHeader(String name) {
        return null;
    }

    public Collection<String> getHeaderNames() {
        return null;
    }

    public Map<String, String> getHeaders() {
        return null;
    }

    public File getFile(String name) {
        return null;
    }

    public Map<String, File> getFiles() {
        return null;
    }

    public Session getSession() {
        return null;
    }

    public void renewSession() {

    }
}
