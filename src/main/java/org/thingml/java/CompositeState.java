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
    protected final Map<String, Region> sessions;//Dynamic regions
    //protected final Map<String, Object> properties;

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
        this.sessions = Collections.synchronizedMap(new HashMap<String, Region>());
        //this.properties = Collections.synchronizedMap(new HashMap<String, Object>());
    }

    public boolean dispatch(final Event e, final Port p) {
        boolean consumed = false;
        for(int i = 0; i<regions.length; i++) {
            consumed = consumed | regions[i].handle(e, p);
        }
        for(Region session : sessions.values()) {
            consumed = consumed | session.handle(e, p);
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

    public Region addSession(String key, CompositeState c) {
        //System.out.println("adding " + key);
        final List<AtomicState> states = new ArrayList<AtomicState>();
        states.add(c);
        final Region r = new Region(c.name, states, c, new ArrayList<Handler>(), false);
        sessions.put(key, r);
        //System.out.println("#dynamic regions: " + sessions.size());
        return r;
    }

    public void removeSession(String key) {
        //System.out.println("removing " + key);
        sessions.remove(key);
        //System.out.println("#dynamic regions: " + sessions.size());
    }

    public boolean isTerminated(String session) {
        return sessions.containsKey(session) && sessions.get(session).current instanceof FinalState;
    }

}
