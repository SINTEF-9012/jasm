package org.thingml.java;

import org.thingml.java.ext.Event;
import org.thingml.java.ext.StateAction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Composite are containers for regions
 *
 * @author bmori
 */
public class CompositeState extends AtomicState {

    Region regions[] = new Region[1];

    public CompositeState(final String name) {
        super(name);
        Region r = new Region("default");
        regions[0] = r;
        this.onEntry = ()->{
            for (final Region reg : regions) {
                reg.onEntry();
            }
        };
        this.onExit = ()->{
            for (final Region reg : regions) {
                reg.onExit();
            }
        };
    }

    public CompositeState(final String name, boolean isEmpty) {
        super(name);
        if (isEmpty) {
            regions = new Region[0];
        } else {
            Region r = new Region("default");
            regions[0] = r;
            this.onEntry = ()->{
                for (final Region reg : regions) {
                    reg.onEntry();
                }
            };
            this.onExit = ()->{
                for (final Region reg : regions) {
                    reg.onExit();
                }
            };
        }
    }

    public CompositeState add(final AtomicState s) {
        regions[0].add(s);
        return this;
    }

    public CompositeState add(final Region r) {
        regions = Arrays.copyOf(regions, regions.length + 1);
        regions[regions.length-1] = r;
        return this;
    }

    public CompositeState initial(final AtomicState s) {
        regions[0].initial(s);
        return this;
    }

    public CompositeState keepHistory(final boolean history) {
        regions[0].keepHistory(history);
        return this;
    }

    public void handle(final Event e, final Status status) {
        boolean consumed = false;
        for(final Region r : regions) {
            r.handle(e, status);
            consumed = consumed | status.consumed;
        }
        if (consumed)
            status.consumed = true;
        if (!status.consumed) {//if not, the composite can (try to) consume it
            super.handle(e, status);
        } else {
            status.next = this;
        }
    }

    public String toString() {
        return "Composite state " + name;
    }

    public void onEntry(final StateAction onEntry) {
        this.onEntry = ()->{
            onEntry.execute();
            for (final Region r : regions) {
                r.onEntry();
            }
        };
    }

    public void onExit(final StateAction onExit) {
        this.onExit = ()->{
            for (final Region r : regions) {
                r.onExit();
            }
            onExit.execute();
        };
    }
}
