package org.thingml.java;

import org.thingml.java.ext.Event;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bmori on 29.04.2014.
 */
public abstract class Component {

    protected CompositeState behavior;
    private final Map<Port, Connector> bindings;
    private final static NullConnector nullConnector = new NullConnector(null, null, null, null);
    public final String name;

    public Component(String name) {
       this.name = name;
       bindings = new HashMap<Port, Connector>();
    }

    abstract protected Component buildBehavior();

    boolean canSend(Event event, Port port) {return port.out.contains(event.getType());}

    boolean canReceive(Event event, Port port) {return port.in.contains(event.getType());}

    public void send(Event event, Port port) {
        Connector c = bindings.get(port);
        if (c == null) c = nullConnector;
        switch (port.type) {
             case PROVIDED:
                 c.onProvided(event);
                 break;
             case REQUIRED:
                 c.onRequired(event);
                 break;

        }
    }

    public void receive(Event event, Port port) {
        behavior.dispatch(event, port);
    }

    public void start() {
        behavior.onEntry();
    }

    public void connect(Port p, Connector c) {
         bindings.put(p, c);
    }

}
