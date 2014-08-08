package org.thingml.java;

import org.thingml.java.ext.Event;
import org.thingml.java.ext.EventType;
import org.thingml.java.ext.IHandlerAction;
import org.thingml.java.ext.NullHandlerAction;

final class NullHandler extends Handler {

    public NullHandler(final EventType event, final IState source) {
        this("", new NullHandlerAction(), event, null, source);
    }

    private NullHandler(final String name, final IHandlerAction action, final EventType event, final Port port, final IState source) {
        super(name, action, event, port, source);
    }

    public IState execute(Event e) {
        //do nothing
        return null;//stay in the same state
    }

}
