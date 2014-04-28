package org.thingml.java;

import java.util.List;
import java.util.logging.Level;
import java.util.stream.Stream;
import org.thingml.java.ext.Event;
import org.thingml.java.ext.IStateAction;
import org.thingml.java.ext.NullEvent;

/**
 * This implementation of CompositeState is suitable for
 *  - multi-threaded platforms, and
 *  - composite containing multiple regions
 * Otherwise, use CompositeStateST, with a lesser overhead
 * @author bmori
 */
public final class CompositeStateMT extends CompositeState {

    public CompositeStateMT(final String name, final List<IState> states, final IState initial, final List<Handler> transitions, final IStateAction action, final List<Region> regions, final boolean keepHistory) {
        super(name, states, initial, transitions, action, regions, keepHistory);
    }

    @Override
    public void onEntry() {
        super.onEntry();
        regions.parallelStream().forEach(r -> r.onEntry());
    }

    @Override
    public void onExit() {
        regions.parallelStream().forEach(r -> r.onExit());
        super.onExit();
    }
    
    @Override
    public Stream<Region> getRegions() {
        return regions.parallelStream();
    }

}
