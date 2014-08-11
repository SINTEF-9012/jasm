package org.thingml.java;

import org.thingml.java.ext.Event;
import org.thingml.java.ext.EventType;

public class Transition extends Handler {

    private final IState target;

    public Transition(final String name, final EventType event, final Port port, final IState source, final IState target) {
        super(name, event, port, source);
        this.target = target;
    }

    public IState execute(final Event e) {
        getSource().onExit();
        doExecute(e);
        target.onEntry();
        return target;
    }

    public IState getTarget() {
        return target;
    }

}
