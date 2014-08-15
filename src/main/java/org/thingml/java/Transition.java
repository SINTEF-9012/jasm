package org.thingml.java;

import org.thingml.java.ext.Event;
import org.thingml.java.ext.EventType;

public class Transition extends Handler {

    private final AtomicState target;

    public Transition(final String name, final EventType event, final Port port, final AtomicState source, final AtomicState target) {
        super(name, event, port, source);
        this.target = target;
    }

    public AtomicState execute(final Event e) {
        getSource().onExit();
        doExecute(e);
        target.onEntry();
        return target;
    }

    public AtomicState getTarget() {
        return target;
    }

}
