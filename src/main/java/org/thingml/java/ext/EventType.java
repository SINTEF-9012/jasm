package org.thingml.java.ext;

public abstract class EventType {

    protected String name = "DEFAULT";

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof EventType) && ((EventType) o).getName().equals(name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
