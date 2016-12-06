package org.thingml.java;

import org.thingml.java.ext.Event;
import org.thingml.java.ext.NullEventType;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by bmori on 29.04.2014.
 */
public abstract class Component/* implements Runnable*/ {
    private String name;
    protected AtomicBoolean active = new AtomicBoolean(true);

    public volatile long forkId = 0;
    public BlockingQueue<Component> forks;
    public Component root = null;

    protected static final Event ne = new NullEventType().instantiate();

    protected CompositeState behavior;
    protected CepDispatcher cepDispatcher;

    public Component() {
        this("default");
    }

    public Component(String name) {
        this.name = name;
        this.cepDispatcher = new CepDispatcher();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    abstract public Component buildBehavior(String session, Component root);

    protected Object lock = "lock";

    public void receive(final Event event) {
            if (active.get()) {
                synchronized (lock) {
                    behavior.dispatch(event);
                    while (active.get() && behavior.dispatch(ne)) {//run empty transition as much as we can, if still active (we might have reach a final state)
                        ;
                    }
                    if (root == null && active.get()) {
                        for (final Component child : forks) {
                            final Event child_e = event.clone();
                            new Thread() {
                                @Override
                                public void run() {
                                    super.run();
                                    child.receive(child_e);
                                }
                            }.start();
                        }
                    }
                    lock.notifyAll();
                }
            }
    }

    public Component init() {
        return init(new LinkedBlockingDeque<Component>(1024));
    }

    public Component init(BlockingQueue<Component> forks) {
        this.forks = forks;
        this.active = new AtomicBoolean(true);
        return this;
    }

    public void addSession(Component session) {
        this.forkId++;
        session.forkId = this.forkId;
        session.root = this;
        session.init(null);
        try {
            if (this.forks.offer(session)) {
                session.start();
            } else {
                session.delete ();
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
            behavior.onEntry();
            while (active.get() && behavior.dispatch(ne)) {//run empty transition as much as we can
                ;
            }
        }
    }

    public void stop() {
        if (forks != null) {
            for (Component child : forks) {
                child.stop();
            }
        }
        active.set(false);
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
        cepDispatcher = null;
        if (root != null) {
            root.forks.remove(this);
            root = null;
        }
    }

    protected void createCepStreams() {}
}
