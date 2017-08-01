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

    public void onEntry(StateAction onEntry) {
        this.onEntry = onEntry;
    }

    public void onExit(StateAction onExit) {
        this.onExit = onExit;
    }

    AtomicState add(Handler h) {
        transitions = Arrays.copyOf(transitions, transitions.length + 1);
        transitions[transitions.length-1] = h;
        return this;
    }

    public AtomicState build() {return this;}

    protected AtomicState handle(Event e, Port p) throws Exception {
        boolean found = false;
        AtomicState next = null;
        for(Handler h : transitions) {
            if (!found && h.check.check(e)) {
                found = true;
                h.action.execute(e);
                next = h.target;
            } else if (found && h.check.check(e)) {
                throw new Exception("Non determinism: Event " + e + " can trigger at least two handlers in " + this);
            }
        }
        return next;
    }

    public String toString() {
        return "Atomic state " + name;
    }

}
