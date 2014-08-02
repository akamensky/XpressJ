package xpressj.server;

/**
 * Created by akamensky on 8/2/14.
 */
public class ResponseImpl implements Response {
    public boolean isConsumed() {
        return false;
    }

    public int getStatusCode() {
        return 0;
    }

    public void setStatusCode(int status) {

    }

    public void send(int code, String body) {

    }

    public void send(String body) {

    }

    public void json(int code, Object obj) {

    }

    public void json(Object obj) {

    }

    public void addCookie(Cookie cookie) {

    }

    public void addCookie(String name, String value) {

    }

    public void unsetCookie(String name) {

    }

    public void addHeader(String name, String value) {

    }
}