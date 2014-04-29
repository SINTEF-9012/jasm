package org.thingml.java;

import org.thingml.java.ext.Event;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bmori on 29.04.2014.
 */
public abstract class Component {

    private final CompositeState behavior;
    private final Map<Port, Connector> bindings;

    public Component() {
       behavior = buildBehavior();
       bindings = new HashMap<>();
    }

    abstract protected CompositeState buildBehavior();

    boolean canSend(Event event, Port port) {return port.out.contains(event.getType());}

    boolean canReceive(Event event, Port port) {return port.in.contains(event.getType());}

    public void send(Event event, Port port) {
        switch (port.type) {
             case PROVIDED:
                 bindings.get(port).onProvided(event);
                 break;
             case REQUIRED:
                 bindings.get(port).onRequired(event);
                 break;
        }
    }

    public void receive(Event event, Port port) {
        behavior.dispatch(event, port);
    }

    public void start() {
        behavior.onEntry();
    }

}
