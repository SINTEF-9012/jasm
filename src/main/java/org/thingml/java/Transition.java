package org.thingml.java;

import org.thingml.java.ext.Event;
import org.thingml.java.ext.EventType;
import org.thingml.java.ext.IHandlerAction;

public final class Transition extends Handler {

    private final IState target;

    public Transition(final String name, final IHandlerAction action, final EventType event, final Port port, final IState source, final IState target) {
        super(name, action, event, port, source);
        this.target = target;
    }

    public IState execute(final Event e) {
        getSource().onExit();
        getAction().execute(e);
        target.onEntry();
        return target;
    }

    public IState getTarget() {
        return target;
    }

}
