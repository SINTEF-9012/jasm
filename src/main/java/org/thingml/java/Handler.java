package org.thingml.java;

import org.thingml.java.ext.Event;
import org.thingml.java.ext.EventType;
import org.thingml.java.ext.IHandlerAction;

public abstract class Handler implements IHandler {

    private final String name;
    private final IHandlerAction action;
    private final EventType event;
    private final Port port;
    private final IState source;

    public Handler(final String name, final IHandlerAction action, final EventType event, final Port port, final IState source) {
        this.name = name;
        this.action = action;
        this.event = event;
        this.port = port;
        this.source = source;
    }

    public boolean check(final Event e, final Port p) {
        return ((p==null) || p.equals(port)) && (e.getType().equals(event)) && action.check(e, event);
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

    public IHandlerAction getAction() {
        return action;
    }
    
    

}
