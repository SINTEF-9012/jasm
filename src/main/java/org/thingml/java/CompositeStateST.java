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
    public void onEntry() {
        log.finest(name + " on entry at " + System.currentTimeMillis());
        super.onEntry();
        regions.stream().forEach(r -> r.onEntry());
    }

    @Override
    public void onExit() {
        log.finest(name + " on exit at " + System.currentTimeMillis());
        regions.stream().forEach(r -> r.onExit());
        super.onExit();
    }
    
    @Override
    public Stream<Region> getRegions() {
        return regions.parallelStream();
    }

}
