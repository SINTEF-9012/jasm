package org.thingml.java;

import org.thingml.java.ext.Event;

/**
 * Created by bmori on 25.02.2016.
 */
public class FinalState extends AtomicState {

    public FinalState(final String name) {
        super(name);
    }

    @Override
    protected final void handle(final Event e, final Port p, final Status s) {}
}
