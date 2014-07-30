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

import xpressj.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by akamensky on 7/30/14.
 */
public class SessionFactory {
    private Map<String, Session> sessions;
    private Configuration configuration;

    public SessionFactory(Configuration configuration) {
        this.configuration = configuration;
        this.sessions = new HashMap<>();
    }

    public Session getSession(String uuid) {
        return this.sessions.get(uuid);
    }

    public Session createSession() {
        String uuid = UUID.randomUUID().toString();
        return createSession(uuid);
    }

    public Session createSession(String uuid) {
        Session session = new SessionImpl(uuid, this.configuration.getSessionMaxAge(), this);
        this.sessions.put(session.getId(), session);
        return session;
    }

    public void expireSession(String uuid) {
        this.sessions.remove(uuid);
    }
}
