package org.thingml.java.ext;

import org.thingml.java.Port;

public abstract class Event {

    private final EventType type;
    private Port port;

    protected Event(final EventType type, final Port port) {
        this.type = type;
        this.port = port;
    }

    public EventType getType() {
        return type;
    }

    public Port getPort() {
        return port;
    }

    public void setPort(final Port port) {
        this.port = port;
    }

    @Override
    public int hashCode() {
        int hash = 1;
        hash = hash * 31 + port.hashCode();
        hash = hash * 31 + type.hashCode();
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
}
