package org.thingml.java;

import org.thingml.java.ext.*;

import java.util.Optional;

public class Handler {

    final static NullEventType nullEventType = new NullEventType();

    EventType event = nullEventType;
    Port port = null;
    AtomicState source;
    AtomicState target;
    HandlerAction action = (Event event)->{};
    HandlerCheck check = (Event event, Port p)->{return ((p==null) ? event.getPort() == null : p.equals(event.getPort())) && this.event.equals(event.getType());};

    public Handler() {}

    public Handler event(EventType event) {
        this.event = event;
        return this;
    }

    public Handler port(Port port) {
        this.port = port;
        return this;
    }

    public Handler from(AtomicState state) {
        this.source = state;
        this.source.add(this);
        this.target = state;
        return this;
    }

    public Handler to(AtomicState state) {
        return this;
    }

    public Handler guard(HandlerCheck c) {
        this.check = (Event event, Port p) -> {
            if (this.event.equals(event.getType())) {
                return c.check(event, port);
            }
            return false;
        };
        return this;
    }

    public Handler action(HandlerAction action) {
        this.action = action;
        return this;
    }

}
