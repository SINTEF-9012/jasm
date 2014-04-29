package org.thingml.java;

import org.thingml.java.ext.Event;
import org.thingml.java.ext.IStateAction;
import org.thingml.java.ext.NullStateAction;

import java.util.logging.Logger;

public class AtomicState implements IState {

    protected final String name;
    protected final IStateAction action;

    private final Logger log = Logger.getLogger(AtomicState.class.getName());

    public AtomicState(final String name) {
        this.name = name;
        this.action = new NullStateAction();
    }

    public AtomicState(final String name, final IStateAction action) {
        this.name = name;
        this.action = action;
    }

    public void onEntry() {
        log.finest(name + " on entry at " + System.currentTimeMillis());
        action.onEntry();
    }

    public void onExit() {
        log.finest(name + " on exit at " + System.currentTimeMillis());
        action.onExit();
    }

    public synchronized IState dispatch(final Event e, Port port, final HandlerHelper helper) {
        final IHandler handler = helper.getActiveHandler(this, e, port);
        return handler.execute(e);
    }

    public String getName() {
        return name;
    }
    
    public String toString() {
        return "Atomic state " + name;
    }
    
}
