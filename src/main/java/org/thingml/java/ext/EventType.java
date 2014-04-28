package org.thingml.java.ext;

public abstract class EventType {

    protected String name = "DEFAULT";

    public String getName() {
        return name;
    }
    
    public Event instantiate() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
