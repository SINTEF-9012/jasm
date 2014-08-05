package org.thingml.java.ext;

import org.thingml.java.Port;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Event {

    private final EventType type;
    private Port port;

    protected Event(final EventType type, final Port port)
    {
        this.type = type;
        this.port = port;
    }

    public EventType getType() {
        return type;
    }

    public Port getPort() {
        return port;
    }

    public void setPort(final Port port) {
        this.port = port;
    }

}
