package org.thingml.java;

import org.thingml.java.ext.Event;
import org.thingml.java.ext.EventType;

public abstract class Handler implements IHandler {

    private final String name;
    private final EventType event;
    private final Port port;
    private final IState source;

    public Handler(final String name, final EventType event, final Port port, final IState source) {
        this.name = name;
        this.event = event;
        this.port = port;
        this.source = source;
    }

    public boolean check(final Event e, final Port p) {
        return ((p == null) || p.equals(port)) && (e.getType().equals(event)) && doCheck(e, p);
    }

    /**
     * Should be overriden to perform more detailed checks on the event (guard)
     *
     * @param e
     * @param p
     * @return
     */
    protected boolean doCheck(final Event e, final Port p) {
        return true;
    }

    protected void doExecute(final Event e) {

    }

    public String getName() {
        return name;
    }

    public IState getSource() {
        return source;
    }

    public EventType getEvent() {
        return event;
    }

}
