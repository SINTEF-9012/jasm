package org.thingml.java.ext;

import org.thingml.java.Port;

public abstract class Event {

    final EventType type;
    Port port;

    protected Event(final EventType type) {
        this.type = type;
    }

    public EventType getType() {
        return type;
    }

    public Port getPort() {
        return port;
    }

    public Event setPort(final Port port) {
        this.port = port;
        return this;//Not a usual setter, but convenient (fluent interface)
    }

    public String toString(String serialization) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int hashCode() {
        int hash = 1;
        hash = hash * 31 + type.hashCode();
        hash = hash * 31 + (port!=null ? port.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Event) {
            final Event eObj = (Event) obj;
            return eObj.getPort().equals(this.getPort())
                    && eObj.getType().equals(this.getType());
        }
        return false;
    }

    abstract public Event clone();

}
