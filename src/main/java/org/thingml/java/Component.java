package org.thingml.java;

import org.thingml.java.ext.Event;
import org.thingml.java.ext.NullEventType;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by bmori on 29.04.2014.
 */
public abstract class Component implements Runnable {
    private String name;
    protected AtomicBoolean active = new AtomicBoolean(true);

    public BlockingQueue<Component> forks;
    public Component root = null;

    private Thread thread;
    protected BlockingQueue<Event> queue;
    protected static final Event ne = new NullEventType().instantiate();

    protected CompositeState behavior;

    public Component() {
        this("default");
    }

    public Component(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    abstract public Component buildBehavior(String session, Component root);

    public synchronized void receive(final Event event, final Port p) {
        if (active.get()) {
            event.setPort(p);
            queue.offer(event);
            for (Component child : (forks!=null)?forks:Collections.<Component>emptyList()) {
                child.receive(event.clone(), p);
            }
        }
    }

    public Component init() {
        return init(new LinkedTransferQueue<Event>(), new LinkedTransferQueue<Component>());
    }

    public Component init(BlockingQueue<Event> queue, BlockingQueue<Component> forks) {
        this.forks = forks;
        this.queue = queue;
        this.active = new AtomicBoolean(true);
        return this;
    }

    public void addSession(Component session) {
        session.root = this;
        session.init(new LinkedTransferQueue<Event>(), null);
        forks.add(session);
        session.start();
    }

    public void start() {
        if (behavior != null) {
            behavior.onEntry();
            thread = new Thread(this);
            thread.start();
        }
    }

    public void stop() {
        for (Component child : (forks!=null)?forks:Collections.<Component>emptyList()) {
            child.stop();
        }
        active.set(false);
        try {
            if (thread != null) {
                thread.join(512);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (behavior != null) {
            behavior.onExit();
        }
    }

    public void delete() {
        if (forks != null) {
            for (Component child : forks) {
                child.delete();
            }
            forks.clear();
            forks = null;
        }
        behavior = null;
        if (queue != null) {
            queue.clear();
            queue = null;
        }
        thread = null;
        if (root != null) {
            root.forks.remove(this);
            root = null;
        }
    }

        @Override
        public void run() {
            while (active.get() && behavior.dispatch(ne, null)) {//run empty transition as much as we can
                ;
            }
            while (active.get()) {
                try {
                    Event e = queue.take();//should block if queue is empty, waiting for a message
                    behavior.dispatch(e, e.getPort());
                    while (active.get() && behavior.dispatch(ne, null)) {//run empty transition as much as we can, if still active (we might have reach a final state)
                        ;
                    }
                     /*   for (Component child : forks) {
                            Event ee = e.clone();
                            child.behavior.dispatch(ee, e.getPort());
                            ee = null;
                            while (child.active.get() && child.behavior.dispatch(ne, null)) {//run empty transition as much as we can, if still active (we might have reach a final state)
                                ;
                            }
                        }*/
                    e = null;
                } catch (InterruptedException e) {
                    //e.printStackTrace();
                }
            }
        }
}
