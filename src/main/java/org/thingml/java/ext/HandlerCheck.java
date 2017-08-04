package org.thingml.java.ext;

@FunctionalInterface
public interface HandlerCheck {
    boolean check(final Event e);
}
