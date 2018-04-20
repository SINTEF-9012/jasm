package no.sintef.jasm;

import no.sintef.jasm.ext.Event;
import no.sintef.jasm.ext.NullEventType;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Lightweight component to encapsulate state machine
 */
public abstract class Component implements Runnable {

    private String name;
    protected volatile CompositeState behavior;
    protected AtomicBoolean active = new AtomicBoolean(true);

    public BlockingQueue<Component> forks;
    public Component root = null;

    private Thread thread;
    protected BlockingQueue<Event> queue;
    protected Event ne = new NullEventType().instantiate();

    /**
     * Default constructor
     */
    public Component() {
        this("default");
    }

    /**
     * Construtor
     * @param name the name for the component
     */
    public Component(String name) {
        this.name = name;
    }

    /**
     *
     * @return the name of this component
     */
    public String getName() {
        return name;
    }

    /**
     * Builds the behavior (i.e. sets the behavior attribute)
     * @param session to identify the session to build (null for the default session)
     * @param root to identify the root component when building a session (null for the default behavior)
     * @return
     */
    abstract public Component buildBehavior(String session, Component root);

    /**
     * Allows to send events to this component
     * @param event the event to be received by this component
     */
    public synchronized void receive(final Event event) {
        if (active.get()) {
            synchronized (forks) {
                queue.offer(event);
                if (root == null && active.get()) {
                    forks.parallelStream().forEach((child) -> {
                        final Event clone = event.clone();
                        clone.setPort(event.getPort());
                        child.receive(clone);
                    });
                }
            }
        }
    }

    /**
     * Initilized this component (setup queue, etc)
     * @return
     */
    public Component init() {
        return init(new LinkedBlockingQueue<>(), new LinkedBlockingDeque<Component>(1024));
    }

    /**
     * Initilized this component (setup queue, etc)
     * @return
     */
    public Component init(final BlockingQueue<Event> queue, final BlockingQueue<Component> forks) {
        this.forks = forks;
        this.queue = queue;
        this.active = new AtomicBoolean(true);
        return this;
    }

    /**
     * Adds a dynamic session to this component
     * @param session the session to add
     */
    public void addSession(Component session) {
        session.root = this;
        session.init(new java.util.concurrent.LinkedBlockingQueue < Event > (256), null);
        synchronized (forks) {
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
                session.delete();
                session = null;
            }
        }
    }

    /**
     * Starts the components, which should be ready and start processing events
     */
    public void start() {
        if (behavior != null) {
            behavior.onEntry.execute();
            thread = new Thread(this);
            thread.start();
        }
    }

    /**
     * Stops the component
     */
    public void stop() {
        active.set(false);
        synchronized (forks) {
            if (forks != null) {
                for (final Component child : forks) {
                    child.stop();
                }
            }
        }
        try {
            if (thread != null) {
                thread.join(100);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        /*if (behavior != null) {
            behavior.onExit.execute();
        }*/
    }

    /**
     * "delete" the component
     */
    public void delete() {
        synchronized (forks) {
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
    }

    /**
     * The main logic that dispatches events
     */
    @Override
    public void run() {
        final Status status = new Status();
        try {
            if (active.get()) {
                behavior.handle(ne, status);
            }
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
