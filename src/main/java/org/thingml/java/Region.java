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

    public Region add(AtomicState s) {
        states = Arrays.copyOf(states, states.length + 1);
        states[states.length-1] = s;
        return this;
    }

    public Region initial(AtomicState s) {
        initial = s;
        current = initial;
        return this;
    }

    public Region keepHistory(boolean history) {
        keepHistory = history;
        return this;
    }

    public boolean handle(final Event e, final Port p) throws Exception {
        final AtomicState next = current.handle(e, p);
        if (next != null) {
            current = next;
            return true;
        }
        return false;
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
