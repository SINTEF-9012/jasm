package org.thingml.java.ext;

import org.thingml.java.Port;

public final class NullEventType extends EventType {

    public NullEventType() {
        name = "NULL";
    }

    public Event instantiate(Port port) {
        return new NullEvent(this, port);
    }
}
