package org.thingml.java;

import org.thingml.java.ext.Event;

import java.util.List;
import java.util.logging.Logger;

/**
 * @author bmori
 */
public final class Region /*implements IState*/ {

    protected final String name;
    protected final AtomicState initial;
    protected final boolean keepHistory;
    protected final List<AtomicState> states;
    protected final List<Handler> transitions;

    protected AtomicState current;
    protected final HandlerHelper helper;

    protected final Logger log = Logger.getLogger(Region.class.getName());

    //private final NullEventType net = new NullEventType();

    public Region(final String name, final List<AtomicState> states, final AtomicState initial, final List<Handler> transitions, final boolean keepHistory) {
        this.name = name;
        this.initial = initial;
        this.keepHistory = keepHistory;
        this.states = states;
        this.transitions = transitions;

        this.current = this.initial;
        this.helper = new HandlerHelper().init(states, transitions);
    }

    public boolean handle(final Event e, final Port p) {
        final AtomicState next = current.handle(e, p, helper);
        if (next != null) {
            current = next;
            return true;
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public void onEntry() {
        if (!keepHistory) {
            current = initial;
        }
        current.onEntry();
    }

    public void onExit() {
        current.onExit();
    }

    public String toString() {
        return "Region " + name;
    }

    public AtomicState getCurrent() {
        return current;
    }
}
