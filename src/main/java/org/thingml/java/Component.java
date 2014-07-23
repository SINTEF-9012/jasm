package org.thingml.java;

import org.thingml.java.ext.Event;
import org.thingml.java.ext.NullEvent;
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
    private String name;

    private final SignedEvent ne;

    private Receiver receiver;
    private Thread receiverT;

    private final BlockingQueue<SignedEvent> queue = new ArrayBlockingQueue<SignedEvent>(1024);

    public Component() {
        ne = new SignedEvent(new NullEventType().instantiate(), null);
        bindings = new HashMap<Port, Connector>();
    }

    public Component(String name) {
       this();
       this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    abstract public Component buildBehavior();

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

    public void receive(Event event, Port p) {
        try {
            queue.put(new SignedEvent(event, p));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        //receive(net.instantiate(), null);//it might be an auto-transition to be triggered right away
        behavior.onEntry();
        receiver = new Receiver();
        receiverT = new Thread(new Receiver());
        receiverT.start();
    }

    public void stop() {
        if (receiver != null) {
            receiver.active = false;
            try {
                receiverT.join(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (behavior != null) {
            behavior.onExit();
        }
    }

    public void connect(Port p, Connector c) {
         bindings.put(p, c);
    }

    private class SignedEvent{//TODO: find a way to get rid of them... without breaking everything :-)
        final Event event;
        final Port port;

        public SignedEvent(Event event, Port port) {
            this.event = event;
            this.port = port;
        }
    }

    private class Receiver implements Runnable {

        boolean active = true;

        @Override
        public void run() {
            queue.offer(ne);
            while(active) {
                try {
                    final SignedEvent se = queue.take();//should block if queue is empty, waiting for a message
                    behavior.dispatch(se.event, se.port);
                    while(behavior.dispatch(ne.event, null)) {
                        //receiverT.sleep(0,1);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
