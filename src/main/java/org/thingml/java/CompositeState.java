package org.thingml.java;

import org.thingml.java.ext.Event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Composite are containers for regions
 *
 * @author bmori
 */
public class CompositeState extends AtomicState {

    AtomicState states[] = new AtomicState[0];
    AtomicState initial;
    Region regions[] = new Region[1];
    boolean keepHistory = false;

    public CompositeState(final String name) {
        super(name);
    }

    public CompositeState add(AtomicState s) {
        states = Arrays.copyOf(states, states.length + 1);
        states[states.length-1] = s;
        return this;
    }

    public CompositeState add(Region r) {
        regions = Arrays.copyOf(regions, regions.length + 1);
        regions[regions.length-1] = r;
        return this;
    }

    public CompositeState initial(AtomicState s) {
        initial = s;
        return this;
    }

    public CompositeState keepHistory(boolean history) {
        keepHistory = history;
        return this;
    }

    public CompositeState build() {
        Region r = new Region("default");
        r.initial(initial).keepHistory(keepHistory);
        for(AtomicState s : states) {
            r.add(s);
        }
        regions[0] = r;
        states = null;
        initial = null;
        return this;
    }

    public boolean dispatch(final Event e, final Port p) throws Exception {
        boolean consumed = false;
        for(Region r : regions) {
            consumed = consumed | r.handle(e, p);
        }
        return consumed;
    }

    public String toString() {
        return "Composite state " + name;
    }

    public void onEntry() {
        onEntry.execute();
        for (Region r : regions) {
            r.onEntry();
        }
    }

    public void onExit() {
        for (Region r : regions) {
            r.onExit();
        }
        onExit.execute();
    }

    protected AtomicState handle(Event e, Port p) throws Exception {
        if (!dispatch(e, p)) {//if not, the composite can (try to) consume it
            return super.handle(e, p);
        } else {
            return this;
        }
    }

}
