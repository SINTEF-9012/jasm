package org.thingml.java;

import org.thingml.java.ext.Event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by bmori on 29.04.2014.
 */
public class Port {

    final String name;
    final Component component;

    private Port[] listeners = new Port[0];

    public Port(final String name, final Component component) {
        this.name = name;
        this.component = component;
    }

    public void addListener(final Port p) {
        listeners = Arrays.copyOf(listeners, listeners.length + 1);
        listeners[listeners.length-1] = p;
    }

    public void removeListener(final Port p) {
        Port[] copy = new Port[listeners.length];
        int i = 0;
        for(final Port port : listeners) {
            if (!port.equals(p)) {
                copy[i] = port;
                i++;
            }
        }
        listeners = Arrays.copyOfRange(copy, 0, i);
    }

    public void send(final Event e) {
        for(final Port p : listeners) {
            Event clone = e.clone();
            clone.setPort(p);
            p.component.receive(e.clone(), p);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Port) {
            final Port p = (Port) o;
            return p.name.equals(name);
        }
        return false;

    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
