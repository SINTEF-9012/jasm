package org.thingml.java.ext;

import org.thingml.java.Port;

@FunctionalInterface
public interface HandlerCheck {
    boolean check(final Event e, final Port p);
}
