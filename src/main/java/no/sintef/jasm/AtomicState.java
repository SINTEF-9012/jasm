package org.thingml.java;

import org.thingml.java.ext.Event;
import org.thingml.java.ext.StateAction;

import java.util.Arrays;

public class AtomicState {

    final String name;
    StateAction onEntry = ()->{};
    StateAction onExit = ()->{};
    Handler[] transitions = new Handler[0];

    public AtomicState(final String name) {
        this.name = name;
    }

    public void onEntry(final StateAction onEntry) {
        this.onEntry = onEntry;
    }

    public void onExit(final StateAction onExit) {
        this.onExit = onExit;
    }

    AtomicState add(final Handler h) {
        transitions = Arrays.copyOf(transitions, transitions.length + 1);
        transitions[transitions.length-1] = h;
        return this;
    }

    public AtomicState build() {return this;}

    protected void handle(final Event e, final Status status) {
        for(final Handler h : transitions) {
            if (h.check.check(e)) {
                h.action.execute(e);
                status.consumed = true;
                status.next = h.target;
                return;
            }
        }
        status.consumed = false;
        status.next = this;
    }

    public String toString() {
        return "Atomic state " + name;
    }

}
