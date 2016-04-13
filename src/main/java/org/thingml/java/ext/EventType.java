package org.thingml.java.ext;

import org.thingml.java.Port;

import java.util.Map;

public abstract class EventType {

    protected String name = "DEFAULT";

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof EventType)  {
            EventType et = (EventType) o;
            return et.getName().equals(name);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public Event instantiate(Port port, Map<String, Object> params) {
        throw new UnsupportedOperationException();
    }

    public Event instantiate(Port port, byte[] payload, String serialization) {
        throw new UnsupportedOperationException();
    }

    public Event instantiate(Port port, String payload, String serialization) {
        throw new UnsupportedOperationException();
    }

    public String toString(Event event, String serialization) {
        throw new UnsupportedOperationException();
    }

    public byte[] toBytes(Event event, String serialization) {
        throw new UnsupportedOperationException();
    }

}
