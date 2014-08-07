package test;

import xpressj.route.Route;
import xpressj.route.RouteMatcher;
import xpressj.server.ServerConfiguration;

/**
 * Created by akamensky on 8/4/14.
 */
public class Configuration implements ServerConfiguration {
    @Override
    public int[] getPorts() {
        return new int[]{8080};
    }

    @Override
    public String getHost() {
        return "0.0.0.0";
    }

    @Override
    public String getStaticFilesLocation() {
        return null;
    }

    @Override
    public String getExternalStaticFilesLocation() {
        return null;
    }

    @Override
    public Route getNotFoundPage() {
        return null;
    }

    @Override
    public Route getErrorPage() {
        return null;
    }

    @Override
    public int getSessionMaxAge() {
        return 0;
    }

    @Override
    public String getSessionCookieName() {
        return null;
    }

    @Override
    public boolean useSessions() {
        return false;
    }

    @Override
    public RouteMatcher getRouteMatcher() {
        return null;
    }

    @Override
    public String getKeystoreFile() {
        return null;
    }

    @Override
    public String getKeystorePassword() {
        return null;
    }

    @Override
    public String getTruststoreFile() {
        return null;
    }

    @Override
    public String getTruststorePassword() {
        return null;
    }
}
