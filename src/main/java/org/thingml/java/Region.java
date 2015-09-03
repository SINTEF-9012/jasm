package org.thingml.java;

import org.thingml.java.ext.Event;

import java.util.List;

/**
 * @author bmori
 */
public final class Region {

    protected final String name;
    protected final AtomicState initial;
    protected final boolean keepHistory;

    protected AtomicState current;
    protected final HandlerHelper helper;

    public Region(final String name, final List<AtomicState> states, final AtomicState initial, final List<Handler> transitions, final boolean keepHistory) {
        this.name = name;
        this.initial = initial;
        this.keepHistory = keepHistory;
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
