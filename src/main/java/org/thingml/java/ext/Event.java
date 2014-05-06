package org.thingml.java.ext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Event {

    private final EventType type;

    protected Event(final EventType type) {
        this.type = type;
    }

    public EventType getType() {
        return type;
    }

}
