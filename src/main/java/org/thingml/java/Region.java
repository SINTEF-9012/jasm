package org.thingml.java;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import org.thingml.java.ext.Event;
import org.thingml.java.ext.NullEvent;

/**
 *
 * @author bmori
 */
public final class Region implements IState {

    protected final String name;
    protected final IState initial;
    protected final boolean keepHistory;
    protected final List<IState> states;
    protected final List<Handler> transitions;

    protected IState current;
    protected final HandlerHelper helper;

    protected final Logger log = Logger.getLogger(Region.class.getName());

    public Region(final String name, final List<IState> states, final IState initial, final List<Handler> transitions, final boolean keepHistory) {
        this.name = name;
        this.initial = initial;
        this.keepHistory = keepHistory;
        this.states = Collections.unmodifiableList(states);
        this.transitions = Collections.unmodifiableList(transitions);

        this.current = this.initial;
        this.helper = new HandlerHelper(states, transitions);
    }

    public synchronized boolean handle(final Event e) {
        log.finest(getName() + " handle " + e.getType().getName());
        IState next = null;
        if (current instanceof CompositeState) {
            CompositeState composite = (CompositeState) current;
            log.finest("          Composite: " + composite.toString());
            if (!composite.dispacth(e)) {
                final IHandler handler = helper.getActiveHandler(current, e);
                log.finest("          Composite: " + current.toString());
                next = handler.execute(e);
                if (null != next) {
                    log.finest("                        OK");
                    current = next;
                    return true;
                }
            }
        } else {//current is an Atomic State
            final IHandler handler = helper.getActiveHandler(current, e);
            log.finest("          Atomic: " + current.toString());
            next = handler.execute(e);
            if (null != next) {
                log.finest("                        OK");
                current = next;
                return true;
            }
        }

        return false;
    }

    public String getName() {
        return name;
    }

    @Override
    public void onEntry() {
        if (!keepHistory) {
            current = initial;
        }
        current.onEntry();
    }

    @Override
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
