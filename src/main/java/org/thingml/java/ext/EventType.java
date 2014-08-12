package org.thingml.java.ext;

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
}
