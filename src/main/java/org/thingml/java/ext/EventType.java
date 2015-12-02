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

    abstract public Event instantiate(Port port, Map<String, Object> params);

}
