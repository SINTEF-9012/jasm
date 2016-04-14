package org.thingml.java.ext;

import org.thingml.java.Port;

import java.util.Map;

public final class NullEventType extends EventType {

    public NullEventType() {
        super("NULL", (short)0);
    }

    public Event instantiate() {
        return new NullEvent(this);
    }

    @Override
    public Event instantiate(Map<String, Object> params) {
        return instantiate();
    }
}
