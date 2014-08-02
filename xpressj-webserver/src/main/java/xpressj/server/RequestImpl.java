package xpressj.server;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by akamensky on 8/2/14.
 */
public class RequestImpl implements Request {
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
