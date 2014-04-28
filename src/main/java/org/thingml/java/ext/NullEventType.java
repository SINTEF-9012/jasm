package org.thingml.java.ext;

public final class NullEventType extends EventType {

    public NullEventType() {
        name = "NULL";
    }
    
    @Override
    public Event instantiate() {
        return new NullEvent(this);
    }
}
