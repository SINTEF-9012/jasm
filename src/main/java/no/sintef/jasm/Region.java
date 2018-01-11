package org.thingml.java;

import org.thingml.java.ext.Event;

import java.util.Arrays;
import java.util.List;

/**
 * @author bmori
 */
public final class Region {

    final String name;
    AtomicState initial;
    boolean keepHistory = false;
    AtomicState[] states = new AtomicState[0];
    AtomicState current;

    public Region(final String name) {
        this.name = name;
    }

    public Region add(final AtomicState s) {
        states = Arrays.copyOf(states, states.length + 1);
        states[states.length-1] = s;
        return this;
    }

    public Region initial(final AtomicState s) {
        initial = s;
        current = initial;
        return this;
    }

    public Region keepHistory(final boolean history) {
        keepHistory = history;
        return this;
    }

    public void handle(final Event e, final Status status) {
        current.handle(e, status);
        current = status.next;
    }

    public void onEntry() {
        if (!keepHistory) {
            current = initial;
        }
        current.onEntry.execute();
    }

    public void onExit() {
        current.onExit.execute();
    }

    public String toString() {
        return "Region " + name;
    }

}
