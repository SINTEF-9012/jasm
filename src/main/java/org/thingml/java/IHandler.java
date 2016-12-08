package org.thingml.java;

import org.thingml.java.ext.Event;

interface IHandler {

    boolean check(final Event e, final Port p);

    AtomicState execute(final Event e);

}
