package org.thingml.java;

import org.thingml.java.ext.IStateAction;

import java.util.List;
import java.util.stream.Stream;

/**
 *
 * @author bmori
 */
public final class CompositeStateST extends CompositeState {

    public CompositeStateST(final String name, final List<IState> states, final IState initial, final List<Handler> transitions, final IStateAction action, final List<Region> regions, final boolean keepHistory) {
        super(name, states, initial, transitions, action, regions, keepHistory);
    }
    
    @Override
    public Stream<Region> getRegions() {
        return regions.parallelStream();
    }

}
