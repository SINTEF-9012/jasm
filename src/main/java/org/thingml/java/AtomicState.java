package org.thingml.java;

import org.thingml.java.ext.Event;

import java.util.logging.Logger;

public class AtomicState {

    protected final String name;

    public int ID;

    private final Logger log = Logger.getLogger(AtomicState.class.getName());

    public AtomicState(final String name) {
        this.name = name;
    }

    public void onEntry() {
    }//by default, do nothing

    public void onExit() {
    }//by default, do nothing

    protected AtomicState handle(Event e, Port p, HandlerHelper helper) {
        return helper.getActiveHandler(this, e, p).execute(e);
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return "Atomic state " + name;
    }
    
    public int getID() {
        return ID;
    }

}
