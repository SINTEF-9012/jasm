package org.thingml.java.ext;

import org.thingml.java.Port;

public final class NullEvent extends Event {

    public NullEvent(EventType type, Port port) {
        super(type, port);
    }

    @Override
    public Event clone() {
        return ((NullEventType)getType()).instantiate(getPort());
    }


}
