package org.thingml.java.ext;

import org.thingml.java.Port;

public final class NullEvent extends Event {

    public NullEvent(EventType type) {
        super(type);
    }

    @Override
    public Event clone() {
        return ((NullEventType)getType()).instantiate();
    }


}
