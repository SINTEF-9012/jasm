package org.thingml.java.ext;

import org.thingml.java.Port;

import java.util.Map;

public final class NullEventType extends EventType {

    public NullEventType() {
        name = "NULL";
    }

    public Event instantiate(Port port) {
        return new NullEvent(this, port);
    }

    @Override
    public Event instantiate(Port port, Map<String, Object> params) {
        return instantiate(port);
    }
}
