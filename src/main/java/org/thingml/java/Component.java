package org.thingml.java;

import org.thingml.java.ext.Event;
import org.thingml.java.ext.NullEventType;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by bmori on 29.04.2014.
 */
public abstract class Component implements Runnable {

    private String name;
    protected CompositeState behavior;
    protected AtomicBoolean active = new AtomicBoolean(true);

    public BlockingQueue<Component> forks;
    public Component root = null;

    private Thread thread;
    protected BlockingQueue<Event> queue;
    protected Event ne = new NullEventType().instantiate();

    public Component() {
        this("default");
    }
    public Component(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    abstract public Component buildBehavior(String session, Component root);

    public synchronized void receive(final Event event) {
        if (active.get()) {
            queue.offer(event);
            if (root == null && active.get()) {
                forks.parallelStream().forEach((child) -> {
                    child.receive(event.clone());
                });
            }
        }
    }

    public Component init() {
        return init(new LinkedBlockingQueue<>(), new LinkedBlockingDeque<Component>(1024));
    }

    public Component init(final BlockingQueue<Event> queue, final BlockingQueue<Component> forks) {
        this.forks = forks;
        this.queue = queue;
        this.active = new AtomicBoolean(true);
        return this;
    }

    public void addSession(Component session) {
        session.root = this;
        session.init(new java.util.concurrent.LinkedBlockingQueue < Event > (256), null);
        try {
            if (this.forks.offer(session)) {
                session.start();
            } else {
                session.delete();
                session = null;
            }
        } catch (Exception ex) {
            System.err.println("Error while starting session: " + ex.getMessage());
            ex.printStackTrace();
            session.delete ();
            session = null;
        }
    }

    public void start() {
        if (behavior != null) {
            behavior.onEntry.execute();
            thread = new Thread(this);
            thread.start();
        }
    }

    public void stop() {
        active.set(false);
        if (forks != null) {
            for (final Component child : forks) {
                child.stop();
            }
        }
        try {
            if (thread != null) {
                thread.join(100);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (behavior != null) {
            behavior.onExit.execute();
        }
    }

    public void delete() {
        if (forks != null) {
            for (final Component child : forks) {
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
        final Status status = new Status();
        try {
            behavior.handle(ne, status);
            while (active.get() && status.consumed) {//run empty transition as much as we can
                status.consumed = false;
                status.next = null;
                behavior.handle(ne, status);
            }
            while (active.get()) {
                try {
                    final Event e = queue.take();//should wait if queue is empty, waiting for a message
                    //System.out.println(name + " processing event " + e.getType().getName() + " received on port " + e.getPort().getName());
                    status.consumed = false;
                    status.next = null;
                    behavior.handle(e, status);
                    if (status.consumed && active.get()) {
                        status.consumed = false;
                        status.next = null;
                        behavior.handle(ne, status);
                        while (active.get() && status.consumed) {//run empty transition as much as we can
                            status.consumed = false;
                            status.next = null;
                            behavior.handle(ne, status);
                        }
                    }
                } catch (InterruptedException e) {
                    //e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
