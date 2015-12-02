package org.thingml.java;

import org.thingml.java.ext.Event;
import org.thingml.java.ext.NullEventType;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedTransferQueue;

/**
 * Created by bmori on 29.04.2014.
 */
public abstract class Component implements Runnable {

    boolean active = true;

    protected CompositeState behavior;
    private String name;

    private final Event ne = new NullEventType().instantiate(null);

    private Thread thread;
    protected BlockingQueue<Event> queue;

    protected CepDispatcher cepDispatcher;

    public Component() {
        this("default");
    }

    public Component(String name) {
        this.name = name;
        cepDispatcher = new CepDispatcher();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    abstract public Component buildBehavior();

    public void receive(Event event, final Port p) {
        if (queue == null) {
            queue = new LinkedTransferQueue<Event>();
        }
        event.setPort(p);
        queue.offer(event);
    }

    public Component init() {
        queue = new LinkedTransferQueue<Event>();
        active = true;
        return this;
    }

    public void start() {
        if (behavior != null)
            behavior.onEntry();
        thread = new Thread(this);
        thread.start();
    }

    public void stop() {
        active = false;
        try {
            thread.join(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (behavior != null) {
            behavior.onExit();
        }
    }

        @Override
        public void run() {
            while (behavior.dispatch(ne, null)) {//run empty transition as much as we can
                ;
            }
            while (active) {
                try {
                    final Event e = queue.take();//should block if queue is empty, waiting for a message
                    behavior.dispatch(e, e.getPort());
                    cepDispatcher.dispatch(e);
                    while (behavior.dispatch(ne, null)) {//run empty transition as much as we can
                        ;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    protected void createCepStreams() {}
}
