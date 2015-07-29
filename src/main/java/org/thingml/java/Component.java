package org.thingml.java;

import org.thingml.java.ext.Event;
import org.thingml.java.ext.NullEventType;

import java.util.concurrent.BlockingQueue;

/**
 * Created by bmori on 29.04.2014.
 */
public abstract class Component implements Runnable {

    boolean active = true;

    protected CompositeState behavior;
    private String name;

    private final Event ne = new NullEventType().instantiate(null);

    private Thread thread;
    protected BlockingQueue<Event> queue;// = new ArrayBlockingQueue<Event>(64);

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

    abstract public Component buildBehavior();

    public void receive(Event event, final Port p) {
        event.setPort(p);
        queue.offer(event);
    }

    public Component init() {
        queue = new java.util.concurrent.ArrayBlockingQueue<Event>(64);
        active = true;
        return this;
    }

    public void start() {
        //receive(net.instantiate(), null);//it might be an auto-transition to be triggered right away
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
                try {
                    thread.sleep(0,1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            while (active) {
                try {
                    final Event e = queue.take();//should block if queue is empty, waiting for a message
                    behavior.dispatch(e, e.getPort());
                    while (behavior.dispatch(ne, null)) {//run empty transition as much as we can
                        thread.sleep(0,1);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
}
