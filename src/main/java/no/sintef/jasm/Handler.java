package org.thingml.java;

import org.thingml.java.ext.*;

import java.util.Optional;

public class Handler {

    final static NullEventType nullEventType = new NullEventType();

    EventType event = nullEventType;
    Port port = null;
    AtomicState source;
    AtomicState target;
    HandlerAction action = (final Event event)->{};
    HandlerCheck check = (final Event event)->{
        //return this.event.equals(event.getType()) && ((event.getPort() != null) ? event.getPort().equals(this.port) : this.port==null);
        //System.out.println("Handler checking " + event);
        //System.out.println(this.event + ".equals(" + event.getType() + ")?" + this.event.equals(event.getType()));
        //System.out.println("event.port = " + event.getPort());
        //System.out.println("this.port = " + this.port);
        if (this.event.equals(event.getType()) && ((event.getPort() != null) ? event.getPort().equals(this.port) : this.port==null)) {
            //System.out.println("OK");
            return true;
        } /*else {
            System.out.println("NOT OK!!!");
        }*/
        return false;
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
            //System.out.println("Handler checking " + event);
            if (this.event.equals(event.getType()) && ((event.getPort() != null) ? event.getPort().equals(this.port) : this.port==null)) {
                //System.out.println("OK");
                return c.check(event);
            } /*else {
                System.out.println("NOT OK!!!");
            }*/
            return false;
        };
        return this;
    }

    public Handler action(HandlerAction action) {
        this.action = action;
        return this;
    }

}
