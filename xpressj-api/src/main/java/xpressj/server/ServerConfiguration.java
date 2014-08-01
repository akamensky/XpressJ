package xpressj.server;

import xpressj.route.Route;
import xpressj.route.RouteMatcher;

/**
 * Created by akamensky on 8/1/14.
 */
public interface ServerConfiguration {
    public static final String NAME = "XpressJ";

    public int getPort();

    public String getHost();

    public String getStaticFilesLocation();

    public String getExternalStaticFilesLocation();

    public Route getNotFoundPage();

    public Route getErrorPage();

    public int getSessionMaxAge();

    public String getSessionCookieName();

    public boolean useSessions();

    public RouteMatcher getRouteMatcher();
}
