package org.thingml.java.ext;

import org.thingml.java.Port;

import java.util.Map;

public final class NullEventType extends EventType {

    public NullEventType() {
        super("NULL", (short)0);
    }

    private static Event instance;

    public Event instantiate() {
        if (instance == null)
            instance = new NullEvent(this);
        return instance;
    }

    @Override
    public Event instantiate(Map<String, Object> params) {
        return instantiate();
    }
}
