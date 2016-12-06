package org.thingml.java;

import org.thingml.java.ext.Event;

/**
 * Created by bmori on 25.02.2016.
 */
public class FinalState extends AtomicState {

    public FinalState(String name) {
        super(name);
    }

    @Override
    public final void onExit() {
    }//, do nothing

    @Override
    protected final AtomicState handle(Event e, HandlerHelper helper) {
        return null;
    }
}
