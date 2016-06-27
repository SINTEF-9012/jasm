package org.thingml.java;

import org.thingml.java.ext.Event;
import org.thingml.java.ext.NullEventType;

import java.util.*;
import java.util.concurrent.*;

/**
 * Created by bmori on 29.04.2014.
 */
public abstract class Component implements Runnable {
    private String name;
    protected volatile boolean active = true;

    public volatile long forkId = 0;
    public BlockingQueue<Component> forks;
    public Component root = null;

    private Thread thread;
    protected BlockingQueue<Event> queue;
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

    public void receive(final Event event, final Port p) {
        if (active) {
            event.setPort(p);
            queue.offer(event);
            if (root == null && active) {
                for (Component child : forks) {
                    final Event child_e = event.clone();
                    child.receive(child_e, p);
                }
            }
        }
    }

    public Component init() {
        return init(new LinkedBlockingDeque<Event>(256), new LinkedBlockingDeque<Component>(1024));
    }

    public Component init(BlockingQueue<Event> queue, BlockingQueue<Component> forks) {
        this.forks = forks;
        this.queue = queue;
        this.active = true;
        return this;
    }

    public void addSession(Component session) {
        this.forkId++;
        session.forkId = this.forkId;
        session.root = this;
        session.init(new java.util.concurrent.LinkedBlockingQueue < Event > (256), null);
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
            thread = new Thread(this);
            thread.start();
        }
    }

    public void stop() {
        if (forks != null) {
            for (Component child : forks) {
                child.stop();
            }
        }
        active = false;

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
        cepDispatcher = null;
        thread = null;
        if (root != null) {
            root.forks.remove(this);
            root = null;
        }
    }

        @Override
        public void run() {
            while (active && behavior.dispatch(ne, null)) {//run empty transition as much as we can
                ;
            }
            long wait = 1;
            while (active) {
                try {
                    final Event e = queue.poll(1, TimeUnit.MILLISECONDS);//should block if queue is empty, waiting for a message
                    if (e != null) {
                        behavior.dispatch(e, e.getPort());
                        if (active)
                            cepDispatcher.dispatch(e);
                        while (active && behavior.dispatch(ne, null)) {//run empty transition as much as we can, if still active (we might have reach a final state)
                            ;
                        }
                        wait = 1;
                    } else {
                        wait = Math.min(wait * 4, 128);
                        Thread.currentThread().sleep(wait);
                    }

                } catch (InterruptedException e) {
                    //e.printStackTrace();
                }
            }
        }

    protected void createCepStreams() {}
}
