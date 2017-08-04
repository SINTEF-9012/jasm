package org.thingml.java.ext;

import java.util.Map;

public abstract class EventType {

    protected final String name;
    protected final short code;

    public EventType() {
        this.name = "DEFAULT";
        this.code = 0;
    }

    public EventType(final String name, final short code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public short getCode() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof EventType)  {
            EventType et = (EventType) o;
            return et.getCode() == code && et.getName().equals(name);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public Event instantiate(Map<String, Object> params) {
        throw new UnsupportedOperationException();
    }
}
