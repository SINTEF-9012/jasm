package org.thingml.java;

import org.thingml.java.ext.Event;
import org.thingml.java.ext.IStateAction;

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

    public CompositeState(final String name, final List<IState> states, final IState initial, final List<Handler> transitions, final IStateAction action) {
        this(name, states, initial, transitions, action, Collections.EMPTY_LIST, false);
    }

    public CompositeState(final String name, final List<IState> states, final IState initial, final List<Handler> transitions, final IStateAction action, final List<Region> regions, final boolean keepHistory) {
        super(name, action);
        Region r = new Region("default", states, initial, transitions, keepHistory);
        List<Region> reg = new ArrayList<Region>(regions);
        reg.add(0, r);//we add the default region first
        this.regions = Collections.unmodifiableList(reg);
    }

    public boolean dispatch(final Event e) {
        boolean status = false;
        for(Region r : regions) {
            status = status | r.handle(e);//bitwise OR, and we need to execute r.handle no matter how
        }
        return status;
    }

    public String toString() {
        return "Composite state " + name;
    }

    public void onEntry() {
        log.finest(this + " on entry at " + System.currentTimeMillis());
        super.onEntry();
        for(Region r : regions) {
            r.onEntry();
        }
    }

    public void onExit() {
        log.finest(this + " on exit at " + System.currentTimeMillis());
        for(Region r : regions) {
            r.onExit();
        }
        super.onExit();
    }

    public List<Region> getRegions() {
        return regions;
    }
}
