package org.thingml.java;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import org.thingml.java.ext.Event;
import org.thingml.java.ext.IStateAction;

/**
 * Composite are containers for regions
 *
 * @author bmori
 */
public abstract class CompositeState extends AtomicState {

    protected final List<Region> regions;

    protected final Logger log = Logger.getLogger(AtomicState.class.getName());

    public CompositeState(final String name, final List<IState> states, final IState initial, final List<Handler> transitions, final IStateAction action) {
        this(name, states, initial, transitions, action, Collections.EMPTY_LIST, false);
    }

    public CompositeState(final String name, final List<IState> states, final IState initial, final List<Handler> transitions, final IStateAction action, final List<Region> regions, final boolean keepHistory) {
        super(name);
        Region r = new Region("default", states, initial, transitions, keepHistory);
        List<Region> reg = new ArrayList<>(regions);
        reg.add(0, r);//we add the default region first
        this.regions = Collections.unmodifiableList(reg);
    }

    public synchronized boolean dispacth(final Event e) {
        final DispatchStatus status = new DispatchStatus();
        getRegions().forEach(r -> {
            log.finest(getName() + ".dispatch");
            status.update(r.handle(e));
        });
        return status.status;
    }

    public String toString() {
        return "Composite state " + name;
    }

    //return a parallel or sequential stream, depending on sub-class (MT or ST).
    public abstract Stream<Region> getRegions();

}
