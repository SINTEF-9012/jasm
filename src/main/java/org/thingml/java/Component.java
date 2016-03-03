package org.thingml.java;

import org.thingml.java.ext.Event;
import org.thingml.java.ext.NullEventType;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedTransferQueue;

/**
 * Created by bmori on 29.04.2014.
 */
public abstract class Component implements Runnable {

    boolean active = true;

    public long forkId = 0;
    public ConcurrentMap<Long, Component> forks = new ConcurrentHashMap<Long, Component>();
    public Component root = null;

    protected CompositeState behavior;
    private String name;

    protected final Event ne = new NullEventType().instantiate(null);

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

    abstract public Component buildBehavior(String session, Component root);

    public void receive(Event event, final Port p) {
        if (queue == null) {
            queue = new LinkedTransferQueue<Event>();
        }
        event.setPort(p);
        queue.offer(event);
        for (Component child : forks.values()) {
            Event child_e = event.clone();
            child.receive(child_e, event.getPort());
        }
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
                    //e.printStackTrace();
                }
            }
        }

    protected void createCepStreams() {}
}
