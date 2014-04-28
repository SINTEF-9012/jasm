package org.thingml.java;

import org.thingml.java.ext.Event;
import org.thingml.java.ext.EventType;
import org.thingml.java.ext.IHandlerAction;

public final class InternalTransition extends Handler {

    public InternalTransition(final String name, final IHandlerAction action, final EventType event, final IState source) {
        super(name, action, event, source);
    }
    
    public IState execute(final Event e) {
        getAction().execute(e);
        return getSource();
    }
}
