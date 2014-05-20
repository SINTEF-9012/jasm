package org.thingml.java;

import org.thingml.java.ext.Event;
import org.thingml.java.ext.NullEventType;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by bmori on 29.04.2014.
 */
public abstract class Component {

    protected CompositeState behavior;
    private final Map<Port, Connector> bindings;
    private final static NullConnector nullConnector = new NullConnector(null, null, null, null);
    public final String name;
    private boolean started = false;
    private final NullEventType net = new NullEventType();

    private final Queue<SignedEvent> queue = new ConcurrentLinkedQueue<SignedEvent>();

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
        if (!started) {
            //System.out.println("Queuing event " + event.getType().getName());
            queue.offer(new SignedEvent(event, port));
            queue.offer(new SignedEvent(net.instantiate(), null));
        } else {
            //System.out.println("Dispatching event " + event.getType().getName());
            behavior.dispatch(event, port);
        }
    }

    public void start() {
        behavior.onEntry();
        started = true;
        SignedEvent se = queue.poll();
        while(se != null) {
            //System.out.println("Unqueuing event " + se.event.getType().getName());
            behavior.dispatch(se.event, se.port);
            se = queue.poll();
        }
        receive(net.instantiate(), null);
    }

    public void connect(Port p, Connector c) {
         bindings.put(p, c);
    }

    private class SignedEvent{
        final Event event;
        final Port port;

        public SignedEvent(Event event, Port port) {
            this.event = event;
            this.port = port;
        }
    }

}
