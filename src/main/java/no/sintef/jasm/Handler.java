package no.sintef.jasm;

import no.sintef.jasm.ext.*;

public class Handler {

    final static NullEventType nullEventType = new NullEventType();

    EventType event = nullEventType;
    Port port = null;
    AtomicState source;
    AtomicState target;
    HandlerAction action = (final Event event)->{};
    HandlerCheck check = (final Event event)->{
        return this.event.equals(event.getType()) && ((event.getPort() != null) ? event.getPort().equals(this.port) : this.port==null);
    };

    public Handler() {}

    public Handler event(final EventType event) {
        this.event = event;
        return this;
    }

    public Handler port(final Port port) {
        this.port = port;
        return this;
    }

    public Handler from(final AtomicState state) {
        this.source = state;
        this.source.add(this);
        this.target = state;
        return this;
    }

    public Handler to(final AtomicState state) {
        return this;
    }

    public Handler guard(final HandlerCheck c) {
        this.check = (final Event event) -> {
            if (this.event.equals(event.getType()) && ((event.getPort() != null) ? event.getPort().equals(this.port) : this.port==null)) {
                return c.check(event);
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
