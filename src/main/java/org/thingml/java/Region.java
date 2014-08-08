package org.thingml.java;

import org.thingml.java.ext.Event;
import org.thingml.java.ext.NullEventType;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author bmori
 */
public final class Region /*implements IState*/ {

    protected final String name;
    protected final IState initial;
    protected final boolean keepHistory;
    protected final List<IState> states;
    protected final List<Handler> transitions;

    protected IState current;
    protected final HandlerHelper helper;

    protected final Logger log = Logger.getLogger(Region.class.getName());

    private final NullEventType net = new NullEventType();

    public Region(final String name, final List<IState> states, final IState initial, final List<Handler> transitions, final boolean keepHistory) {
        this.name = name;
        this.initial = initial;
        this.keepHistory = keepHistory;
        this.states = Collections.unmodifiableList(states);
        this.transitions = Collections.unmodifiableList(transitions);

        this.current = this.initial;
        this.helper = new HandlerHelper().init(states, transitions);
    }

    public boolean handle(final Event e, final Port p) {
        IState next = null;
        if (current instanceof CompositeState) {
            final CompositeState c = (CompositeState) current;
            boolean consumed = false;
            for (Region r : c.regions) {//we check if a region can consume the event
                consumed = consumed | r.handle(e, p);//using the bitwise operator on boolean as we do not want a shortcut (all regions have to handle the event)
            }
            if (!consumed) {//if not, the composite can (try to) consume it
                next = helper.getActiveHandler(c, e, p).execute(e);
            }
        } else {
            next = helper.getActiveHandler(current, e, p).execute(e);
        }
        if (next != null) {
            current = next;
            return true;
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public void onEntry() {
        if (!keepHistory) {
            current = initial;
        }
        current.onEntry();
    }

    public void onExit() {
        current.onExit();
    }

    public String toString() {
        return "Region " + name;
    }

    public IState getCurrent() {
        return current;
    }
}
