package org.thingml.java.ext;

import org.thingml.java.Port;

public final class NullEvent extends Event {

    protected NullEvent(EventType type, Port port) {
        super(type, port);
    }
}
