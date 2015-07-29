package org.thingml.java;

import org.thingml.java.ext.Event;
import org.thingml.java.ext.EventType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bmori on 29.04.2014.
 */
public class Port {

    final PortType type;
    final String name;
    final List<EventType> in, out;
    final Component component;

    private List<Port> listeners = new ArrayList<Port>();

    public Port(final PortType type, final String name, final List<EventType> in, final List<EventType> out, final Component component) {
        this.type = type;
        this.name = name;
        this.in = in;
        this.out = out;
        this.component = component;
    }

    public void addListener(Port p) {
        listeners.add(p);
    }

    public void removeListener(Port p) {
        listeners.remove(p);
    }

    public void send(Event e) {
        for(Port p : listeners) {
            p.component.receive(e, p);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Port) {
            final Port p = (Port) o;
            return p.name.equals(name) && p.type.equals(type) && p.in.equals(in) && p.out.equals(out);
        }
        return false;

    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + in.hashCode();
        result = 31 * result + out.hashCode();
        return result;
    }

    public String getName() {
        return name;
    }
}
