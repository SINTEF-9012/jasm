package org.thingml.java;

import org.thingml.java.ext.Event;

interface IHandler {

    default boolean check(final Event e) {
        return true;
    }

    IState execute(final Event e);

}
