package org.thingml.java.ext;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Event {

    protected Map<String, Object> params = new ConcurrentHashMap<>();

    private final EventType type;

    protected Event(final EventType type) {
        this.type = type;
    }

    public EventType getType() {
        return type;
    }

    public Object get(String param) {
        return params.get(param);
    }

}
