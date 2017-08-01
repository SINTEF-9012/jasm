package org.thingml.java.ext;

@FunctionalInterface
public interface HandlerAction {
    void execute(Event e);
}
