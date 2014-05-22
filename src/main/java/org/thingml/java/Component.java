package org.thingml.java;

import org.thingml.java.ext.Event;
import org.thingml.java.ext.NullEventType;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by bmori on 29.04.2014.
 */
public abstract class Component {

    protected CompositeState behavior;
    private final Map<Port, Connector> bindings;
    private final static NullConnector nullConnector = new NullConnector(null, null, null, null);
    public final String name;
    private final NullEventType net = new NullEventType();
    private Thread receiver;

    private final BlockingQueue<SignedEvent> queue = new ArrayBlockingQueue<SignedEvent>(1024);

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
        try {
            queue.put(new SignedEvent(event, port));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        //receive(net.instantiate(), null);//it might be an auto-transition to be triggered right away
        behavior.onEntry();
        receiver = new Thread(new Receiver(this));
        receiver.start();
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

    private class Receiver implements Runnable {

        private Component cpt;

        public Receiver(Component cpt) {
            this.cpt = cpt;
        }

        @Override
        public void run() {
            queue.offer(new SignedEvent(net.instantiate(), null));
            while(true) {
                try {
                    final SignedEvent se = queue.take();//should block if queue is empty, waiting for a message
                    boolean status = behavior.dispatch(se.event, se.port);
                    do { //check empty transition until they can not more be triggered
                        status = behavior.dispatch(net.instantiate(), null);
                    } while(status);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
