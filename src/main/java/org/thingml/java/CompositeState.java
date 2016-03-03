package org.thingml.java;

import org.thingml.java.ext.Event;

import java.util.*;

/**
 * Composite are containers for regions
 *
 * @author bmori
 */
public class CompositeState extends AtomicState {

    public final Region[] regions;

    public CompositeState(final String name, final List<AtomicState> states, final AtomicState initial, final List<Handler> transitions) {
        this(name, states, initial, transitions, Collections.EMPTY_LIST, false);
    }

    public CompositeState(final String name, final List<AtomicState> states, final AtomicState initial, final List<Handler> transitions, final List<Region> regions, final boolean keepHistory) {
        super(name);
        Region r = new Region("default", states, initial, transitions, keepHistory);
        List<Region> reg = new ArrayList<Region>(regions);
        reg.add(0, r);//we add the default region first
        this.regions = new Region[reg.size()];
        int i = 0;
        for(Region re : reg) {
            this.regions[i] = re;
            i++;
        }
    }

    public boolean dispatch(final Event e, final Port p) {
        System.out.println("Dispatching " + e.getType().getName() + " to " + regions.length + " region(s)");
        boolean consumed = false;
        for(int i = 0; i<regions.length; i++) {
            consumed = consumed | regions[i].handle(e, p);
        }
        return consumed;
    }

    public String toString() {
        return "Composite state " + name;
    }

    public void onEntry() {
        super.onEntry();
        for (Region r : regions) {
            r.onEntry();
        }
    }

    public void onExit() {
        for (Region r : regions) {
            r.onExit();
        }
        super.onExit();
    }

    protected AtomicState handle(Event e, Port p, HandlerHelper helper) {
        if (!dispatch(e, p)) {//if not, the composite can (try to) consume it
            return super.handle(e, p, helper);
        } else {
            return this;
        }
    }

}
