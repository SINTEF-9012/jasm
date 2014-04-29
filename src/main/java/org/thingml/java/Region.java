package org.thingml.java;

import org.thingml.java.ext.Event;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author bmori
 */
public final class Region /*implements IState*/ {

    protected final String name;
    protected final IState initial;
    protected final boolean keepHistory;
    protected final List<IState> states;
    protected final List<Handler> transitions;

    protected IState current;
    protected final HandlerHelper helper;

    protected final Logger log = Logger.getLogger(Region.class.getName());

    public Region(final String name, final List<IState> states, final IState initial, final List<Handler> transitions, final boolean keepHistory) {
        this.name = name;
        this.initial = initial;
        this.keepHistory = keepHistory;
        this.states = Collections.unmodifiableList(states);
        this.transitions = Collections.unmodifiableList(transitions);

        this.current = this.initial;
        this.helper = new HandlerHelper(states, transitions);
    }

    public synchronized boolean handle(final Event e, final Port port) {
        //log.finest(getName() + " handle " + e.getType().getName());
        IState next = current.dispatch(e, port, helper);
        if (null != next) {
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

    public IState getCurrent() {
        return current;
    }
}
