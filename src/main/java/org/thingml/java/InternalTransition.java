package org.thingml.java;

import org.thingml.java.ext.Event;
import org.thingml.java.ext.EventType;

public class InternalTransition extends Handler {

    public InternalTransition(final String name, final EventType event, final Port port, final IState source) {
        super(name, event, port, source);
    }

    public IState execute(final Event e) {
        doExecute(e);
        return getSource();
    }
}
