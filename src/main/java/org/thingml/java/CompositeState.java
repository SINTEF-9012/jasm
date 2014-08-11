package org.thingml.java;

import org.thingml.java.ext.Event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 * Composite are containers for regions
 *
 * @author bmori
 */
public class CompositeState extends AtomicState {

    protected final List<Region> regions;

    protected final Logger log = Logger.getLogger(AtomicState.class.getName());

    public CompositeState(final String name, final List<IState> states, final IState initial, final List<Handler> transitions) {
        this(name, states, initial, transitions, Collections.EMPTY_LIST, false);
    }

    public CompositeState(final String name, final List<IState> states, final IState initial, final List<Handler> transitions, final List<Region> regions, final boolean keepHistory) {
        super(name);
        Region r = new Region("default", states, initial, transitions, keepHistory);
        List<Region> reg = new ArrayList<Region>(regions);
        reg.add(0, r);//we add the default region first
        this.regions = reg;
    }

    public boolean dispatch(final Event e, final Port p) {
        boolean consumed = false;
        for (Region r : regions) {
            consumed = consumed | r.handle(e, p);//bitwise OR, and we need to execute r.handle no matter how
        }
        return consumed;
    }

    public String toString() {
        return "Composite state " + name;
    }

    public void onEntry() {
        log.finest(this + " on entry at " + System.currentTimeMillis());
        super.onEntry();
        for (Region r : regions) {
            r.onEntry();
        }
    }

    public void onExit() {
        log.finest(this + " on exit at " + System.currentTimeMillis());
        for (Region r : regions) {
            r.onExit();
        }
        super.onExit();
    }

    protected IState handle(Event e, Port p, HandlerHelper helper) {
        if (!dispatch(e, p)) {//if not, the composite can (try to) consume it
            return super.handle(e, p, helper);
        } else {
            return this;
        }
    }

    public List<Region> getRegions() {
        return regions;
    }
}
