package org.thingml.java;

import org.thingml.java.ext.Event;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bmori on 29.04.2014.
 */
public class Port {

    final PortType type;
    final String name;
    final Component component;

    private List<Port> listeners = new ArrayList<Port>();

    public Port(final PortType type, final String name, final Component component) {
        this.type = type;
        this.name = name;
        this.component = component;
    }

    public void addListener(Port p) {
        listeners.add(p);
    }

    public void removeListener(Port p) {
        listeners.remove(p);
    }

    public void send(final Event e) {
        for(final Port p : listeners) {
            final Event c = e.clone();
            c.setPort(p);
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    p.component.receive(c);
                }
            }.start();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Port) {
            final Port p = (Port) o;
            return p.name.equals(name) && p.type.equals(type);
        }
        return false;

    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }

    public String getName() {
        return name;
    }
}
